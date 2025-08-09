package controller.noti;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import controller.noti.NotiEventBroker;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.noti.NotificationDao;
import model.noti.NotificationDto;

@WebServlet(value = "/sse", asyncSupported = true)
public class NotiSseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usersId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Object userNumObj = session.getAttribute("usersNum");
        if (!(userNumObj instanceof Long usersNum) || usersNum <= 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Connection", "keep-alive");

        // 비동기 시작 + 구독 등록
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(0);
        NotiEventBroker.getInstance().subscribe(usersNum, asyncContext);

        // 1) 재연결 지시 간격 (20초)
        PrintWriter out = response.getWriter();
        out.write("retry: 20000\n\n");
        out.flush();

        // 2) 초기에 한번 현재 상태 스냅샷을 전송 (선택: lastEventId 고려 가능)
        long lastNotiNum = 0L;
        String lastEventId = request.getHeader("Last-Event-ID"); // 클라이언트가 설정했다면 활용
        if (lastEventId != null) {
            try { lastNotiNum = Long.parseLong(lastEventId); } catch (NumberFormatException ignore) {}
        }

        List<NotificationDto> initList =
                NotificationDao.getInstance().notiSelectAfter(usersNum, lastNotiNum);
        int unreadCount = NotificationDao.getInstance().notiReadCount(usersNum);

        // init 스냅샷 전송 (기존 onmessage로 그대로 소화 가능)
        if (!initList.isEmpty()) {
            // 기존 스키마: 배열 + 각 객체에 readCount 삽입
            StringBuilder sb = new StringBuilder();
            sb.append("event: noti\n");
            sb.append("data: [");
            for (int i = 0; i < initList.size(); i++) {
                NotificationDto dto = initList.get(i);
                String json = toJsonWithCount(dto, unreadCount);
                sb.append(json);
                if (i < initList.size() - 1) sb.append(",");
            }
            sb.append("]\n\n");
            out.write(sb.toString());
            out.flush();
        } else {
            // 연결 유지용 주석 라인 1회
            out.write(": init\n\n");
            out.flush();
        }
    }

    private String toJsonWithCount(NotificationDto dto, int readCount) {
        // org.json.simple 사용 없이 간단 문자열 조립 (원한다면 json-simple 사용해도 됨)
        String msg = dto.getNotiMessage() == null ? "" : dto.getNotiMessage().replace("\"","\\\"");
        String writer = dto.getNotiCommentWriter() == null ? "" : dto.getNotiCommentWriter().replace("\"","\\\"");
        String content = dto.getNotiCommentContent() == null ? "" : dto.getNotiCommentContent().replace("\"","\\\"");
        String stayName = dto.getNotiStayName() == null ? "" : dto.getNotiStayName().replace("\"","\\\"");
        String inqTitle = dto.getNotiInqTitle() == null ? "" : dto.getNotiInqTitle().replace("\"","\\\"");
        String inqContent = dto.getNotiInqContent() == null ? "" : dto.getNotiInqContent().replace("\"","\\\"");
        String imageName = dto.getNotiImageName() == null ? "" : dto.getNotiImageName().replace("\"","\\\"");

        return "{"
                + "\"notiNum\":" + dto.getNotiNum()
                + ",\"senderNum\":" + dto.getNotiSenderNum()
                + ",\"message\":\"" + msg + "\""
                + ",\"readCode\":" + dto.getNotiReadCode()
                + ",\"createdAt\":\"" + dto.getNotiCreatedAt() + "\""
                + ",\"imageType\":\"" + (dto.getNotiImageType()==null? "":dto.getNotiImageType()) + "\""
                + ",\"typeCode\":" + dto.getNotiTypeCode()
                + ",\"type\":\"" + (dto.getNotiType()==null? "":dto.getNotiType()) + "\""
                + ",\"daysAgo\":\"" + (dto.getNotiDaysAgo()==null? "":dto.getNotiDaysAgo()) + "\""
                + ",\"bookNum\":\"" + (dto.getNotiBookNum()==null? "":dto.getNotiBookNum()) + "\""
                + ",\"bookCheckIn\":\"" + (dto.getNotiCheckIn()==null? "":dto.getNotiCheckIn()) + "\""
                + ",\"bookCheckOut\":\"" + (dto.getNotiCheckOut()==null? "":dto.getNotiCheckOut()) + "\""
                + ",\"stayNum\":" + dto.getNotiStayNum()
                + ",\"stayName\":\"" + stayName + "\""
                + ",\"commentUsersNum\":" + dto.getNotiCommentUsersNum()
                + ",\"commentWriter\":\"" + writer + "\""
                + ",\"commentContent\":\"" + content + "\""
                + ",\"commentParentNum\":\"" + (dto.getNotiCommentParentNum()==null? "":dto.getNotiCommentParentNum()) + "\""
                + ",\"inqNum\":" + dto.getNotiInqNum()
                + ",\"inqTitle\":\"" + inqTitle + "\""
                + ",\"inqContent\":\"" + inqContent + "\""
                + ",\"imageName\":\"" + imageName + "\""
                + ",\"readCount\":" + readCount
                + "}";
    }
}