package controller.book;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import controller.noti.NotiEventBroker;
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
	        NotificationDao notiDao = NotificationDao.getInstance();

	        List<ExpiredBookingDto> expiredList = bookDao.findExpiredBook(conn);
	        List<NotificationDto> pushList = new ArrayList<>();

	        try {
	            for (ExpiredBookingDto booking : expiredList) {
	                String bookNum = booking.getBookNum();
	                long bookUsersNum = booking.getBookUsersNum();
	                long bookStayNum = booking.getBookStayNum();

	                bookDao.deleteByBookNum2(conn, bookNum);

	                NotificationDto notiDto = new NotificationDto();
	                notiDto.setNotiRecipientNum(bookUsersNum);
	                notiDto.setNotiSenderNum(bookStayNum);
	                notiDto.setNotiTypeCode(11);
	                notiDto.setNotiTargetTypeCode(10);
	                notiDto.setNotiTargetNum(bookNum);
	                notiDto.setNotiMessage("예약 취소(미결제)");
	                notiDto.setNotiImageType("stay");

	                boolean isNotiSuccess = notiDao.notiBookUpdate(conn, notiDto);
	                if (isNotiSuccess) {
	                    pushList.add(notiDto);
	                }
	            }

	            conn.commit();

	        } catch (Exception innerEx) {
	            conn.rollback(); // 실패 시 롤백
	            throw innerEx;   // 바깥 catch로 다시 던져서 로깅
	        }

	        // 커밋 후 SSE 푸시
	        for (NotificationDto dto : pushList) {
	            long usersNum = dto.getNotiRecipientNum();
	            long notiNum = dto.getNotiNum();

	            NotificationDto full = notiDao.notiSelectOne(usersNum, notiNum);
	            int unreadCount = notiDao.notiReadCount(usersNum);

	            if (full != null) {
	                NotiEventBroker.getInstance().publish(usersNum, List.of(full), unreadCount);
	            }
	        }

	        if (!expiredList.isEmpty()) {
	            System.out.println("기한이 만료된 예약 " + expiredList.size() + "건 삭제 및 알림 전송 완료");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}