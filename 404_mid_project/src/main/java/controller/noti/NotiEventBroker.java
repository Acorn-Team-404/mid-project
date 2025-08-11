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
	
	// 싱글톤 패턴
    private static final NotiEventBroker INSTANCE = new NotiEventBroker();

    // 유저별 SSE 연결을 보관하는 자료구조 선언 (같은 유저가 여러 탭에서 접속할 수 있으므로 중복된 등록을 방지할 수 있는 Set으로 관리)
    // Long에는 usersNum이 들어감
    // 유저가 SSE 연결 시 해당 유저 키에 Set이 없으면 만들고, 있다면 기존 것을 사용
    private final ConcurrentHashMap<Long, CopyOnWriteArraySet<AsyncContext>> subscribers = new ConcurrentHashMap<>();

    // 주기적으로 작업을 실행하기 위한 스레드 풀을 담을 필드
    // ScheduledExecutorService : 일정 시간 간격 또는 지연 실행이 필요한 작업을 스레드에서 돌릴 수 있게 함
    private final ScheduledExecutorService scheduler;

    private NotiEventBroker() {
    	// 스레드 풀 생성
        scheduler = Executors.newScheduledThreadPool(
            1, // 하트비트 처리용
            r -> { // 스레드 팩토리를 사용하여 정의(Runnable 타입)
                Thread t = new Thread(r, "SSE-Heartbeat"); // 스레드 이름 정의
                t.setDaemon(true); // 데몬 스레드(메인 스레드 종료 시 함께 종료)
                return t;
            }
        );
        // 주기적으로 실행할 메서드 정의
        // 브로커 생성 후 25초 대기한 후 heartbeatAll() 실행. 그 후 매 25초마다 반복 실행
        scheduler.scheduleAtFixedRate(this::heartbeatAll, 25, 25, TimeUnit.SECONDS);
    }

    // NotiEventBroker를 참조하기 위한 메서드
    public static NotiEventBroker getInstance() {
        return INSTANCE;
    }

    
    // 연결 등록 및 연결 종료 시 자동 해제 
    public void subscribe(long usersNum, AsyncContext asyncContext) {
    	// 키는 usersNum, 값은 해당 유저의 SSE 연결 set
    	// 해당 유저의 키가 없으면 새로운 Set을 만들고, 있다면 기존 Set 반환
        subscribers.computeIfAbsent(usersNum, key -> new CopyOnWriteArraySet<>()).add(asyncContext);
        
        // 수명 관리
        asyncContext.addListener(new AsyncListener() {
            @Override public void onComplete(AsyncEvent event) { unsubscribe(usersNum, asyncContext); } // 정상 종료 시 제거
            @Override public void onTimeout(AsyncEvent event)  { unsubscribe(usersNum, asyncContext); } // 타임아웃 시 제거
            @Override public void onError(AsyncEvent event)    { unsubscribe(usersNum, asyncContext); } // 예외 발생 시 제거
            @Override public void onStartAsync(AsyncEvent event) { /* no-op */ } // SSE에선 보통 할 일 없음
        });
    }

    // 끊어진 SSE 연결을 정리하는 메서드
    // 여러 기기에서 접속하다가 하나가 끊겨도 나머지 유지
    public void unsubscribe(long usersNum, AsyncContext asyncContext) {
        Set<AsyncContext> set = subscribers.get(usersNum);
        if (set != null) {
            set.remove(asyncContext); // 유저의 해당 연결만 제거
            if (set.isEmpty()) { // 남은 연결이 하나도 없다면
                subscribers.remove(usersNum); // 유저 자체를 제거
            }
        }
        try { asyncContext.complete(); } catch (Exception ignore) {} // 비동기 사이클을 종료하고 리소스 해제
    }

    
    // 데이터를 SSE 형식으로 전송하는 메서드
    public void publish(long usersNum, Iterable<NotificationDto> notis, int unreadCount) {
        Set<AsyncContext> conns = subscribers.get(usersNum); // 유저별로 열린 SSE 채널 집합 가져오기
        if (conns == null || conns.isEmpty()) return; // 비어있다면 종료

        // 데이터를 JSON으로 array에 담기
        JSONArray array = new JSONArray();
        for (NotificationDto dto : notis) {
            JSONObject obj = dtoToJson(dto);
            obj.put("readCount", unreadCount);
            array.add(obj);
        }

        // SSE 규격에 맞게 조립. JS에서 addEventListener("noti", ...)로 가져올 수 있음
        String payload = "event: noti\n" + "data: " + array.toJSONString() + "\n\n";

        // 모든 연결에 전송
        for (AsyncContext asyncCtx : conns) {
            try {
                HttpServletResponse resp = (HttpServletResponse) asyncCtx.getResponse();
                PrintWriter out = resp.getWriter();
                out.write(payload); // 조립한 문자열 삽입
                out.flush(); // 전송
                // 연결이 끊기거나 예외 발생 시 정리
                if (out.checkError()) unsubscribe(usersNum, asyncCtx);
            } catch (Exception e) {
                unsubscribe(usersNum, asyncCtx);
            }
        }
    }

    
    // 읽지 않은 알림 개수를 SSE 형식으로 전송하는 메서드
    public void publishCountOnly(long usersNum, int unreadCount) {
        Set<AsyncContext> conns = subscribers.get(usersNum); // 유저별로 열린 SSE 채널 집합 가져오기
        if (conns == null || conns.isEmpty()) return; // 비어있다면 종료

        JSONObject obj = new JSONObject();
        obj.put("readCount", unreadCount); // 읽지 않은 알림 개수만 삽입
        // SSE 규격에 맞게 조립. JS에서 addEventListener("count", ...)로 가져올 수 있음
        String payload = "event: count\n" + "data: " + obj.toJSONString() + "\n\n";

        // 모든 연결에 전송
        for (AsyncContext asyncContext : conns) {
            try {
                HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                PrintWriter out = resp.getWriter();
                out.write(payload); // 조립한 문자열 삽입
                out.flush(); // 전송
                // 연결이 끊기거나 예외 발생 시 정리
                if (out.checkError()) unsubscribe(usersNum, asyncContext);
            } catch (Exception e) {
                unsubscribe(usersNum, asyncContext);
            }
        }
    }
    

    // 모든 열린 SSE 연결에 주기적으로 하트비트를 보내서 타임아웃되지 않게 하는 메서드
    private void heartbeatAll() {
    	// 유저별로 열려있는 모든 AsyncContext를 순회
        subscribers.forEach((user, conns) -> {
            for (AsyncContext asyncContext : conns) {
                try {
                    HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                    PrintWriter out = resp.getWriter();
                    out.write(": ping\n\n"); // :으로 시작하는 라인은 SSE 주석. 데이터 흐름만 만들어 연결을 유지할 목적
                    out.flush();
                    // 연결이 끊기거나 예외 발생 시 정리
                    if (out.checkError()) unsubscribe(user, asyncContext);
                } catch (Exception e) {
                    unsubscribe(user, asyncContext);
                }
            }
        });
    }

    
    // dto 데이터를 Object에 담는 메서드
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

        // 리뷰 타입
        obj.put("stayNumForReview", tmp.getNotiStayNumForReview());
        obj.put("reviewStayName", tmp.getNotiReviewStayName());
        
        // 문의 타입
        obj.put("inqNum", tmp.getNotiInqNum());
        obj.put("inqTitle", tmp.getNotiInqTitle());
        obj.put("inqContent", tmp.getNotiInqContent());

        // 이미지 table
        obj.put("imageName", tmp.getNotiImageName());
        return obj;
    }
}