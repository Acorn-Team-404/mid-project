package controller.book;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import model.book.BookDao;
import model.book.BookDto;
import model.book.ExpiredBookingDto;
import model.util.DBConnector;


public class BookCleanupTask extends TimerTask{
	
		@Override
	    public void run() {
	        try (Connection conn = DBConnector.getConn()) {
	            conn.setAutoCommit(false);

	            BookDao bookDao = BookDao.getInstance();
	          
	            // 만료된 예약 조회
	            List<ExpiredBookingDto> expiredList = bookDao.findExpiredBook(conn);

	            for (ExpiredBookingDto booking : expiredList) {
	            	
	                String bookNum = booking.getBookNum();
	                long bookUsersNum = booking.getBookUsersNum();

	                // 예약 삭제
	                bookDao.deleteByBookNum(conn, bookNum);

	                // 알림 저장
	              
	                // SSE 전송 
	                
	            }

	            conn.commit();
	            if (!expiredList.isEmpty()) {
	                System.out.println("기한이 만료된 예약 " + expiredList.size() + "건 삭제 및 알림 전송 완료");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
