package controller.noti;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.noti.NotificationDao;
import model.noti.NotificationDto;

@WebServlet(value = "/sse", asyncSupported = true) // 비동기 처리 설정
public class NotiSseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// 세션 검증
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usersId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 세션 타입 검증
        Object userNumObj = session.getAttribute("usersNum");
        if (userNumObj == null || !(userNumObj instanceof Long)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 세션 숫자 검증
        long usersNum = (Long) userNumObj;
        if (usersNum <= 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Connection", "keep-alive");

        // 비동기 처리 선언
        AsyncContext asyncContext = request.startAsync(); // AsyncContext 객체를 생성하고 연결 유지
        asyncContext.setTimeout(0); // 비동기 타임아웃 무제한
        
        // 마지막 알림 번호 변수 (여러 쓰레드에서 동시에 사용하기 위해 AtomicLong 사용)
        AtomicLong lastNotiNum = new AtomicLong(0);
        AtomicBoolean completed = new AtomicBoolean(false); // 중복 종료 방지 (예외 시 딱 한 번만 호출될 예정)
        long startTime = System.currentTimeMillis(); // 타임아웃 커스텀을 위한 연결 시작 시간 변수

        // 데몬쓰레드(true) -> 메인 쓰레드가 종료되면 같이 종료되는 백그라운드 작업용 쓰레드
        // 브라우저나 세션이 종료되면 쓰레드도 함께 끊어주기 위해 데몬쓰레드 사용
        Timer timer = new Timer(true);

        
        // 60초 마다 새 알림을 전송하고 연결이 없다면 하트비트 전송
        timer.scheduleAtFixedRate(new TimerTask() {
            private boolean retrySent = false;

            @Override
            public void run() {
            	if (completed.get()) {
                    timer.cancel();
                    return;
                }
            	
                try {
                    if (System.currentTimeMillis() - startTime > 1000 * 60 * 10) { // SSE 타임아웃 10분
                        System.out.println("⏳ SSE 연결 10분 초과 → 종료");
                        if (completed.compareAndSet(false, true)) {
                            timer.cancel(); // 알림을 전송하던 TimerTask 중지
                            asyncContext.complete(); // SSE 연결 종료
                        }
                        return;
                    }

                    // 비동기 객체로부터 실제 응답 객체 가져오기
                    HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                    PrintWriter out = resp.getWriter(); // 텍스트 데이터 전송을 위한 출력 스트림

                    if (!retrySent) {
                        out.write("retry: 60000\n"); //재연결 간격을 60초로 설정
                        out.flush();
                        retrySent = true;
                    }

                    List<NotificationDto> notiList = NotificationDao.getInstance()
                            .notiSelectAfter(usersNum, lastNotiNum.get());

                    if (!notiList.isEmpty() && lastNotiNum.get() < notiList.get(0).getNotiNum()) {
                        JSONArray jsonArray = new JSONArray();
                        for (NotificationDto tmp : notiList) {
                            JSONObject obj = new JSONObject();
                            // noti 필수 속성
                            obj.put("notiNum", tmp.getNotiNum());
                            obj.put("senderNum", tmp.getNotiSenderNum());
                            obj.put("message", tmp.getNotiMessage());
                            obj.put("readCode", tmp.getNotiReadCode());
                            obj.put("createdAt", tmp.getNotiCreatedAt());
                            obj.put("imageType", tmp.getNotiImageType());
                            obj.put("typeCode", tmp.getNotiTypeCode());
                            
                            // noti 추가 속성
                            obj.put("type", tmp.getNotiType());
                            obj.put("daysAgo", tmp.getNotiDaysAgo());
                            obj.put("readCount", tmp.getNotiReadCount());
                            
                            // 예약 타입
                            obj.put("bookNum", tmp.getNotiBookNum());
                            obj.put("bookCheckIn", tmp.getNotiCheckIn());
                            obj.put("bookCheckOut", tmp.getNotiCheckOut());
                            obj.put("stayNum", tmp.getNotiStayNum());
                            obj.put("stayName", tmp.getNotiStayName());
                            
                            // 댓글 타입
                            obj.put("commentUsersNum", tmp.getNotiCommentUsersNum());
                            obj.put("commentWriter", tmp.getNotiCommentWriter());
                            obj.put("commentContent", tmp.getNotiCommentContent());
                            obj.put("commentParentNum", tmp.getNotiCommentParentNum());
                            
                            // 문의 타입
                            obj.put("inqNum", tmp.getNotiInqNum());
                            obj.put("inqTitle", tmp.getNotiInqTitle());
                            obj.put("inqContent", tmp.getNotiInqContent());
                            
                            // 이미지 table
                            obj.put("imageName", tmp.getNotiImageName());
                            jsonArray.add(obj);
                        }

                        // 현재 알림 목록 중 가장 큰 알림번호 저장
                        long maxNum = notiList.stream()
                                .mapToLong(NotificationDto::getNotiNum)
                                .max()
                                .orElse(lastNotiNum.get());
                        lastNotiNum.set(maxNum); // lastNotiNum에 저장

                        out.write("data: " + jsonArray.toJSONString() + "\n\n");
                        out.flush();

                        if (out.checkError()) {
                            System.out.println("❌ 클라이언트 연결 끊김");
                            if (completed.compareAndSet(false, true)) {
                                timer.cancel();
                                asyncContext.complete();
                            }
                        }

                    } else {
                        out.write(": heartbeat\n\n"); // 새 알림이 없어도 연결을 유지하기 위한 메세지 전송
                        out.flush();

                        if (out.checkError()) {
                            System.out.println("❌ 클라이언트 연결 끊김 (heartbeat)");
                            if (completed.compareAndSet(false, true)) {
                                timer.cancel();
                                asyncContext.complete();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("SSE 예외 발생 → 연결 종료");
                    if (completed.compareAndSet(false, true)) {
                        timer.cancel();
                        asyncContext.complete();
                    }
                }
            }
        }, 0, 60000); // 처음 실행은 즉시, 이후에는 60초마다 run() 메서드 실행
    }
}
