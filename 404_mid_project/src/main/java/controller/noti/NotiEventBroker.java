package controller.noti;

import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.noti.NotificationDto;

public class NotiEventBroker {
    private static final NotiEventBroker INSTANCE = new NotiEventBroker();

    // 유저별 다중 연결(탭) 지원
    private final ConcurrentHashMap<Long, CopyOnWriteArraySet<AsyncContext>> subscribers = new ConcurrentHashMap<>();

    // 하트비트(keep-alive)용 스케줄러
    private final ScheduledExecutorService scheduler;

    private NotiEventBroker() {
        scheduler = Executors.newScheduledThreadPool(
            1,
            r -> {
                Thread t = new Thread(r, "SSE-Heartbeat");
                t.setDaemon(true);
                return t;
            }
        );
        // 25초마다 전체 하트비트 전송 (중간 프락시 타임아웃 방지)
        scheduler.scheduleAtFixedRate(this::heartbeatAll, 25, 25, TimeUnit.SECONDS);
    }

    public static NotiEventBroker getInstance() {
        return INSTANCE;
    }

    public void subscribe(long usersNum, AsyncContext ctx) {
        subscribers.computeIfAbsent(usersNum, k -> new CopyOnWriteArraySet<>()).add(ctx);

        // 연결 수명 정리: 완료/에러/타임아웃 시 구독 해제
        ctx.addListener(new AsyncListener() {
            @Override public void onComplete(AsyncEvent event) { unsubscribe(usersNum, ctx); }
            @Override public void onTimeout(AsyncEvent event)  { unsubscribe(usersNum, ctx); }
            @Override public void onError(AsyncEvent event)    { unsubscribe(usersNum, ctx); }
            @Override public void onStartAsync(AsyncEvent event) { /* no-op */ }
        });
    }

    public void unsubscribe(long usersNum, AsyncContext ctx) {
        Set<AsyncContext> set = subscribers.get(usersNum);
        if (set != null) {
            set.remove(ctx);
            if (set.isEmpty()) {
                subscribers.remove(usersNum);
            }
        }
        // 안전 종료
        try { ctx.complete(); } catch (Exception ignore) {}
    }

    // 알림 생성 시 호출 → 해당 유저의 모든 연결로 즉시 푸시
    public void publish(long usersNum, Iterable<NotificationDto> notis, int unreadCount) {
        Set<AsyncContext> conns = subscribers.get(usersNum);
        if (conns == null || conns.isEmpty()) return;

        // 기존 프런트 스키마를 유지: 배열 형태에 각 객체마다 readCount 포함
        JSONArray array = new JSONArray();
        for (NotificationDto dto : notis) {
            JSONObject obj = dtoToJson(dto);
            obj.put("readCount", unreadCount);
            array.add(obj);
        }

        // 커스텀 이벤트명 사용 가능. (기존 onmessage만 써도 동작함)
        String payload = "event: noti\n" + "data: " + array.toJSONString() + "\n\n";

        for (AsyncContext ctx : conns) {
            try {
                HttpServletResponse resp = (HttpServletResponse) ctx.getResponse();
                PrintWriter out = resp.getWriter();
                out.write(payload);
                out.flush();
                if (out.checkError()) unsubscribe(usersNum, ctx);
            } catch (Exception e) {
                unsubscribe(usersNum, ctx);
            }
        }
    }

    // 읽음 카운트만 갱신이 필요할 때(옵션)
    public void publishCountOnly(long usersNum, int unreadCount) {
        Set<AsyncContext> conns = subscribers.get(usersNum);
        if (conns == null || conns.isEmpty()) return;

        JSONObject obj = new JSONObject();
        obj.put("readCount", unreadCount);
        String payload = "event: count\n" + "data: " + obj.toJSONString() + "\n\n";

        for (AsyncContext ctx : conns) {
            try {
                HttpServletResponse resp = (HttpServletResponse) ctx.getResponse();
                PrintWriter out = resp.getWriter();
                out.write(payload);
                out.flush();
                if (out.checkError()) unsubscribe(usersNum, ctx);
            } catch (Exception e) {
                unsubscribe(usersNum, ctx);
            }
        }
    }

    private void heartbeatAll() {
        subscribers.forEach((user, conns) -> {
            for (AsyncContext ctx : conns) {
                try {
                    HttpServletResponse resp = (HttpServletResponse) ctx.getResponse();
                    PrintWriter out = resp.getWriter();
                    out.write(": ping\n\n"); // SSE 주석 라인 → keep-alive
                    out.flush();
                    if (out.checkError()) unsubscribe(user, ctx);
                } catch (Exception e) {
                    unsubscribe(user, ctx);
                }
            }
        });
    }

    private JSONObject dtoToJson(NotificationDto tmp) {
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
        return obj;
    }
}