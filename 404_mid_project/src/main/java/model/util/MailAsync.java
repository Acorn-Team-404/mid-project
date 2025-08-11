package model.util;

import java.util.concurrent.*;

public class MailAsync {

    // 데몬 스레드로 만들어 톰캣 종료 방해 X
    private static final ExecutorService POOL = new ThreadPoolExecutor(
        2, 4, 60, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(100),
        r -> {
            Thread t = new Thread(r, "mail-sender");
            t.setDaemon(true);
            return t;
        }
    );

    public static void sendAsync(String to, String code) {
        POOL.submit(() -> {
            try { MailUtil.sendEmail(to, code); }
            catch (Exception e) { e.printStackTrace(); /* TODO: 로그/재시도 */ }
        });
    }
	
}
