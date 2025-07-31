package controller.noti;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.noti.NotificationDao;
import model.noti.NotificationDto;



@WebServlet("/sse")
public class NotiSseServlet extends HttpServlet {
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 알림 데이터 조회 (임시로 recipient_id = 3 고정)
        List<NotificationDto> list = NotificationDao.getInstance().notiSelectAll();

        // JSON 배열 생성
        JSONArray jsonArray = new JSONArray();

        for (NotificationDto dto : list) {
            JSONObject obj = new JSONObject();
            // String이어야 함
            /*obj.put("createdAt", dto.getNotiCreatedAt());
            obj.put("senderId", String.valueOf(dto.getNotiSenderNum()));
            obj.put("message", dto.getNotiMessage());
            obj.put("typeGroupId", dto.getNotiTypeGroupId());*/
            
            obj.put("createdAt", "2025-07-02 ~ 2025-08-27");
            obj.put("senderId", "비로소 한옥");
            obj.put("message", "예약 확정");
            obj.put("typeGroupId", "reservation");
            
            jsonArray.add(obj);
        }
        

        // SSE 응답 설정
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        // JSON 문자열 전송
        PrintWriter out = response.getWriter();
        out.write("data: " + jsonArray.toJSONString() + "\n\n");
        out.flush();
    }
}
