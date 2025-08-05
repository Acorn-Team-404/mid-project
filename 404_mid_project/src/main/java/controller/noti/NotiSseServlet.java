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
        if (userNumObj == null || !(userNumObj instanceof Long)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

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

        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(0);

        AtomicLong lastNotiNum = new AtomicLong(0);
        AtomicBoolean completed = new AtomicBoolean(false); // ✅ 중복 종료 방지
        long startTime = System.currentTimeMillis();

        Timer timer = new Timer(true); // 데몬 스레드

        timer.scheduleAtFixedRate(new TimerTask() {
            private boolean retrySent = false;

            @Override
            public void run() {
                try {
                    if (System.currentTimeMillis() - startTime > 1000 * 60 * 10) {
                        System.out.println("⏳ SSE 연결 10분 초과 → 종료");
                        if (completed.compareAndSet(false, true)) {
                            asyncContext.complete();
                            timer.cancel();
                        }
                        return;
                    }

                    HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                    PrintWriter out = resp.getWriter();

                    if (!retrySent) {
                        out.write("retry: 60000\n");
                        out.flush();
                        retrySent = true;
                    }

                    List<NotificationDto> notiList = NotificationDao.getInstance()
                            .notiSelectAfter(usersNum, lastNotiNum.get());

                    if (!notiList.isEmpty() && lastNotiNum.get() < notiList.get(0).getNotiNum()) {
                        JSONArray jsonArray = new JSONArray();
                        for (NotificationDto tmp : notiList) {
                            JSONObject obj = new JSONObject();
                            obj.put("notiNum", tmp.getNotiNum());
                            obj.put("senderNum", tmp.getNotiSenderNum());
                            obj.put("message", tmp.getNotiMessage());
                            obj.put("readCode", tmp.getNotiReadCode());
                            obj.put("createdAt", tmp.getNotiCreatedAt());
                            obj.put("typeCode", tmp.getNotiTypeCode());
                            obj.put("type", tmp.getNotiType());
                            obj.put("daysAgo", tmp.getNotiDaysAgo());
                            obj.put("readCount", tmp.getNotiReadCount());
                            obj.put("bookCheckIn", tmp.getNotiCheckIn());
                            obj.put("bookCheckOut", tmp.getNotiCheckOut());
                            obj.put("stayName", tmp.getNotiStayName());
                            obj.put("commentWriter", tmp.getNotiCommentWriter());
                            obj.put("commentContent", tmp.getNotiCommentContent());
                            obj.put("commentParentNum", tmp.getNotiCommentParentNum());
                            jsonArray.add(obj);
                        }

                        long maxNum = notiList.stream()
                                .mapToLong(NotificationDto::getNotiNum)
                                .max()
                                .orElse(lastNotiNum.get());
                        lastNotiNum.set(maxNum);

                        out.write("data: " + jsonArray.toJSONString() + "\n\n");
                        out.flush();

                        if (out.checkError()) {
                            System.out.println("❌ 클라이언트 연결 끊김");
                            if (completed.compareAndSet(false, true)) {
                                asyncContext.complete();
                                timer.cancel();
                            }
                        }

                    } else {
                        out.write(": heartbeat\n\n");
                        out.flush();

                        if (out.checkError()) {
                            System.out.println("❌ 클라이언트 연결 끊김 (heartbeat)");
                            if (completed.compareAndSet(false, true)) {
                                asyncContext.complete();
                                timer.cancel();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("SSE 예외 발생 → 연결 종료");
                    if (completed.compareAndSet(false, true)) {
                        asyncContext.complete();
                        timer.cancel();
                    }
                }
            }
        }, 0, 60000);
    }
}
