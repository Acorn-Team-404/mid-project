package controller.book;

import java.util.Timer;
import java.util.TimerTask;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitListenerServlet implements ServletContextListener {
	private Timer timer;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		timer = new Timer(true); // 데몬 스레드
		// 0초 후 시작, 1분마다 실행
		timer.scheduleAtFixedRate(new BookCleanupTask(), 0, 60 * 1000);
		System.out.println("예약 취소 스케줄러 시작");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (timer != null) {
			timer.cancel();
			System.out.println("예약 취소 스케줄러 종료");
		}
	}
}
