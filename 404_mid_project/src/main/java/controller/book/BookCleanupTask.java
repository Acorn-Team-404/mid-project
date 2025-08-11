package controller.book;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import model.book.BookDao;
import model.book.BookDto;
import model.book.ExpiredBookingDto;
import model.noti.NotificationDao;
import model.noti.NotificationDto;
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
	                long bookStayNum = booking.getBookStayNum();

	                // 예약 삭제
	                bookDao.deleteByBookNum2(conn, bookNum);

	                // 알림 저장
	                // SSE 전송 

	                long notiRecipientNum = bookUsersNum;
					long notiSenderNum = bookStayNum;
					int notiTypeCode = 11;
					int notiTargetTypeCode = 10;
					String notiTargetNum = bookNum;
					String notiMessage = "예약 취소(미결제)";
					
					NotificationDto notiDto = new NotificationDto();
					
					notiDto.setNotiRecipientNum(notiRecipientNum);
					notiDto.setNotiSenderNum(notiSenderNum);
					notiDto.setNotiTypeCode(notiTypeCode);
					notiDto.setNotiTargetTypeCode(notiTargetTypeCode);
					notiDto.setNotiTargetNum(notiTargetNum);
					notiDto.setNotiMessage(notiMessage);
					notiDto.setNotiImageType("stay");
					
					boolean isNotiSuccess = NotificationDao.getInstance().notiBookUpdate(conn,notiDto);
					
					if(isNotiSuccess) {
						System.out.println("알림 데이터 저장 성공");
					} else {
						System.out.println("알림 데이터 저장 실패");
					}
	                
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
