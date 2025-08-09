package model.noti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import controller.noti.NotiEventBroker;

import model.util.DBConnector;

public class NotificationDao {
	
	
	
	// Connection Pool 관리
	private static NotificationDao notiDao;
	static {
		notiDao = new NotificationDao();
	}
	private NotificationDao() {}
	public static NotificationDao getInstance() {
		return notiDao;
	}
	
	
	
	
	// --------- 실험중
	
	// 새로 추가: 시퀀스에서 다음 noti_num 미리 가져오기
	private long nextNotiNum(Connection conn) throws Exception {
	    try (PreparedStatement ps = conn.prepareStatement("SELECT noti_seq.NEXTVAL FROM dual");
	         ResultSet rs = ps.executeQuery()) {
	        if (rs.next()) return rs.getLong(1);
	        throw new IllegalStateException("Failed to get next noti_seq");
	    }
	}

	// 새로 추가: noti_num 1건으로 상세 조회 (기존 조인 로직 재사용)
	public NotificationDto notiSelectOne(long usersNum, long notiNum) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    NotificationDto dto = null;
	    try {
	        conn = DBConnector.getConn();
	        String sql = """
	            SELECT 
	                -- (기존 notiSelectAfter와 동일한 SELECT 컬럼들)
	                n.noti_num, NVL(n.noti_sender_num, 0) AS noti_sender_num,
	                c.cc_description AS noti_type, n.noti_message, n.noti_read_code,
	                TO_CHAR(n.noti_created_at, 'YYYY-MM-DD') AS noti_created_at,
	                n.noti_image_type, n.noti_type_code,
	                CASE
	                  WHEN (SYSDATE - n.noti_created_at) * 24 * 60 < 1 THEN '방금'
	                  WHEN (SYSDATE - n.noti_created_at) * 24 * 60 < 60
	                       THEN TO_CHAR(FLOOR((SYSDATE - n.noti_created_at) * 24 * 60)) || '분 전'
	                  WHEN (SYSDATE - n.noti_created_at) < 1
	                       THEN TO_CHAR(FLOOR((SYSDATE - n.noti_created_at) * 24)) || '시간 전'
	                  WHEN (SYSDATE - n.noti_created_at) < 365
	                       THEN TO_CHAR(TRUNC(SYSDATE - n.noti_created_at)) || '일 전'
	                  ELSE TO_CHAR(TRUNC((SYSDATE - n.noti_created_at) / 365)) || '년 전'
	                END AS noti_days_ago,
	                (SELECT COUNT(noti_read_code) FROM notifications
	                  WHERE noti_read_code = 10 AND noti_recipient_num = ?) AS noti_read_count,
	                b.book_num AS noti_book_num,
	                TO_CHAR(b.book_checkin_date, 'YYYY-MM-DD') AS noti_book_checkin,
	                TO_CHAR(b.book_checkout_date, 'YYYY-MM-DD') AS noti_book_checkout,
	                s.stay_num AS noti_stay_num, s.stay_name AS noti_stay_name,
	                comm.comment_content AS noti_comment_content,
	                comm.comment_parent_num AS noti_comment_parent_num,
	                comm.comment_writer AS noti_comment_users_num,
	                u.users_id AS noti_comment_writer,
	                inq.inq_num AS noti_inq_num, inq.inq_title AS noti_inq_title,
	                inq.inq_content AS noti_inq_content,
	                img.image_saved_name AS noti_image_name
	            FROM notifications n
	            LEFT JOIN booking b
	              ON n.noti_type_code = 10 AND n.noti_target_num = b.book_num
	            LEFT JOIN stay s
	              ON n.noti_type_code = 10 AND b.book_stay_num = s.stay_num
	            LEFT JOIN comments comm
	              ON n.noti_type_code = 20
	             AND n.noti_target_num = TO_CHAR(comm.comment_num)
	             AND n.noti_sender_num = comm.comment_writer
	             AND n.noti_recipient_num = comm.comment_target_writer
	            LEFT JOIN users u
	              ON n.noti_type_code = 20 AND n.noti_sender_num = u.users_num
	            LEFT JOIN inquiry inq
	              ON n.noti_type_code = 40 AND n.noti_target_num = TO_CHAR(inq.inq_num)
	            LEFT JOIN image_file img
	              ON img.image_target_type = n.noti_image_type
	             AND (
	                   (n.noti_image_type = 'stay' AND img.image_target_id = s.stay_num AND img.image_sort_order = 1)
	                OR (n.noti_image_type = 'profile' AND img.image_target_id = u.users_num AND img.image_sort_order = 1)
	             )
	            LEFT JOIN common_code c
	              ON c.cc_group_id = 'NOTI_TYPE' AND c.cc_code = n.noti_type_code
	            WHERE n.noti_num = ?
	            """;
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, usersNum);
	        pstmt.setLong(2, notiNum);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            dto = new NotificationDto();
	            // rs → dto 매핑 (기존과 동일)
	            dto.setNotiNum(rs.getLong("noti_num"));
	            dto.setNotiSenderNum(rs.getLong("noti_sender_num"));
	            dto.setNotiMessage(rs.getString("noti_message"));
	            dto.setNotiReadCode(rs.getInt("noti_read_code"));
	            dto.setNotiCreatedAt(rs.getString("noti_created_at"));
	            dto.setNotiTypeCode(rs.getInt("noti_type_code"));
	            dto.setNotiImageType(rs.getString("noti_image_type"));

	            dto.setNotiType(rs.getString("noti_type"));
	            dto.setNotiDaysAgo(rs.getString("noti_days_ago"));
	            dto.setNotiReadCount(rs.getInt("noti_read_count"));

	            dto.setNotiBookNum(rs.getString("noti_book_num"));
	            dto.setNotiCheckIn(rs.getString("noti_book_checkin"));
	            dto.setNotiCheckOut(rs.getString("noti_book_checkout"));
	            dto.setNotiStayNum(rs.getLong("noti_stay_num"));
	            dto.setNotiStayName(rs.getString("noti_stay_name"));

	            dto.setNotiCommentContent(rs.getString("noti_comment_content"));
	            dto.setNotiCommentParentNum(rs.getString("noti_comment_parent_num"));
	            dto.setNotiCommentUsersNum(rs.getLong("noti_comment_users_num"));
	            dto.setNotiCommentWriter(rs.getString("noti_comment_writer"));

	            dto.setNotiInqNum(rs.getLong("noti_inq_num"));
	            dto.setNotiInqTitle(rs.getString("noti_inq_title"));
	            dto.setNotiInqContent(rs.getString("noti_inq_content"));

	            dto.setNotiImageName(rs.getString("noti_image_name"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBConnector.close(rs, pstmt, conn);
	    }
	    return dto;
	}
	
	// --------- 실험중
	
	
	// SELECT 쿼리
	public List<NotificationDto> notiSelectAfter(long usersNum, long lastNotiNum) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<NotificationDto> list = new ArrayList<>(); // row를 담을 빈 배열

		try {
			conn = DBConnector.getConn();
			String sql = """
					SELECT 
					    n.noti_num, 
					    NVL(n.noti_sender_num, 0) AS noti_sender_num,
					    c.cc_description AS noti_type,
					    n.noti_message, 
					    n.noti_read_code, 
					    TO_CHAR(n.noti_created_at, 'YYYY-MM-DD') AS noti_created_at,
					    n.noti_image_type,
					    n.noti_type_code,
					    CASE
					    	WHEN (SYSDATE - n.noti_created_at) * 24 * 60 < 1 THEN '방금'
					    	WHEN (SYSDATE - n.noti_created_at) * 24 * 60 < 60
					    		THEN TO_CHAR(FLOOR((SYSDATE - n.noti_created_at) * 24 * 60)) || '분 전'
					    	WHEN (SYSDATE - n.noti_created_at) < 1
					    		THEN TO_CHAR(FLOOR((SYSDATE - n.noti_created_at) * 24)) || '시간 전'
					    	WHEN (SYSDATE - n.noti_created_at) < 365
					    		THEN TO_CHAR(TRUNC(SYSDATE - n.noti_created_at)) || '일 전'
					    	ELSE TO_CHAR(TRUNC((SYSDATE - n.noti_created_at) / 365)) || '년 전'
					    END AS noti_days_ago,
					    (SELECT COUNT(noti_read_code)
						FROM notifications
						WHERE noti_read_code = 10
							AND noti_recipient_num = ?) AS noti_read_count,
						b.book_num AS noti_book_num,
					    TO_CHAR(b.book_checkin_date, 'YYYY-MM-DD') AS noti_book_checkin,
					    TO_CHAR(b.book_checkout_date, 'YYYY-MM-DD') AS noti_book_checkout,
					    s.stay_num AS noti_stay_num,
					    s.stay_name AS noti_stay_name,
					    comm.comment_content AS noti_comment_content,
					    comm.comment_parent_num AS noti_comment_parent_num,
					    comm.comment_writer AS noti_comment_users_num,
					    u.users_id AS noti_comment_writer,
					    inq.inq_num AS noti_inq_num,
						inq.inq_title AS noti_inq_title,
						inq.inq_content AS noti_inq_content,
						img.image_saved_name AS noti_image_name
					FROM notifications n
					LEFT JOIN booking b
					  ON n.noti_type_code = 10 
					 AND n.noti_target_num = b.book_num
					LEFT JOIN stay s
					  ON n.noti_type_code = 10 
					  AND b.book_stay_num = s.stay_num
					LEFT JOIN comments comm
					  ON n.noti_type_code = 20 
					   AND n.noti_target_num = TO_CHAR(comm.comment_num)
						 AND n.noti_sender_num = comm.comment_writer
						 AND n.noti_recipient_num = comm.comment_target_writer
					LEFT JOIN users u
						ON n.noti_type_code = 20
						AND n.noti_sender_num = u.users_num
					LEFT JOIN inquiry inq
						ON n.noti_type_code = 40 
						AND n.noti_target_num = TO_CHAR(inq.inq_num)
					LEFT JOIN image_file img
					  ON img.image_target_type = n.noti_image_type
					 AND (
					      (n.noti_image_type = 'stay'
					       AND img.image_target_id = s.stay_num
					       AND img.image_sort_order = 1)
					   OR (n.noti_image_type = 'profile'
					       AND img.image_target_id = u.users_num
					       AND img.image_sort_order = 1)
					    )
					LEFT JOIN common_code c
					  ON c.cc_group_id = 'NOTI_TYPE'
					 AND c.cc_code = n.noti_type_code
					WHERE n.noti_recipient_num = ?
						AND n.noti_num > ?
					ORDER BY n.noti_num ASC
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, usersNum);
			pstmt.setLong(2, usersNum);
			pstmt.setLong(3, lastNotiNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				NotificationDto dto = new NotificationDto();
				// 사진도 추가해야함
				dto.setNotiNum(rs.getLong("noti_num"));
				dto.setNotiSenderNum(rs.getLong("noti_sender_num"));
				dto.setNotiMessage(rs.getString("noti_message"));
				dto.setNotiReadCode(rs.getInt("noti_read_code"));
				dto.setNotiCreatedAt(rs.getString("noti_created_at"));
				dto.setNotiTypeCode(rs.getInt("noti_type_code"));
				dto.setNotiImageType(rs.getString("noti_image_type"));
				
				// 공통 추가필드
				dto.setNotiType(rs.getString("noti_type"));
				dto.setNotiDaysAgo(rs.getString("noti_days_ago"));
				dto.setNotiReadCount(rs.getInt("noti_read_count"));
				
				// 예약 추가필드
				dto.setNotiBookNum(rs.getString("noti_book_num"));
				dto.setNotiCheckIn(rs.getString("noti_book_checkin"));
				dto.setNotiCheckOut(rs.getString("noti_book_checkout"));
				dto.setNotiStayNum(rs.getLong("noti_stay_num"));
				dto.setNotiStayName(rs.getString("noti_stay_name"));
				
				// 댓글 추가필드
				dto.setNotiCommentContent(rs.getString("noti_comment_content"));
				dto.setNotiCommentParentNum(rs.getString("noti_comment_parent_num"));
				dto.setNotiCommentUsersNum(rs.getLong("noti_comment_users_num"));
				dto.setNotiCommentWriter(rs.getString("noti_comment_writer"));
				
				// 문의 추가필드
				dto.setNotiInqNum(rs.getLong("noti_inq_num"));
				dto.setNotiInqTitle(rs.getString("noti_inq_title"));
				dto.setNotiInqContent(rs.getString("noti_inq_content"));
				
				// 이미지 추가필드
				dto.setNotiImageName(rs.getString("noti_image_name"));
				
				
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
	
	
	
	
	// 수정: notiInsert → insert 성공 후 즉시 푸시
	public boolean notiInsert(NotificationDto dto) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int rowCount = 0;

	    try {
	        conn = DBConnector.getConn();
	        conn.setAutoCommit(false);

	        long newNotiNum = nextNotiNum(conn);

	        String sql = """
	            INSERT INTO notifications (
	                noti_num, noti_recipient_num, noti_sender_num, noti_type_code,
	                noti_target_type_code, noti_target_num, noti_message, noti_image_type
	            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
	        """;
	        pstmt = conn.prepareStatement(sql);

	        pstmt.setLong(1, newNotiNum);
	        pstmt.setLong(2, dto.getNotiRecipientNum());
	        pstmt.setLong(3, dto.getNotiSenderNum());
	        pstmt.setInt(4, dto.getNotiTypeCode());
	        pstmt.setInt(5, dto.getNotiTargetTypeCode());
	        pstmt.setString(6, dto.getNotiTargetNum());
	        pstmt.setString(7, dto.getNotiMessage());
	        pstmt.setString(8, dto.getNotiImageType());

	        rowCount = pstmt.executeUpdate();

	        if (rowCount > 0) {
	            conn.commit();

	            long usersNum = dto.getNotiRecipientNum();
	            NotificationDto full = notiSelectOne(usersNum, newNotiNum); // 방금 insert 건 조인해서 완성
	            int unreadCount = notiReadCount(usersNum);

	            if (full != null) {
	                // 🔴 여기서 즉시 푸시된다
	                NotiEventBroker.getInstance().publish(usersNum, java.util.List.of(full), unreadCount);
	            }
	        } else {
	            conn.rollback();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        try { if (conn != null) conn.rollback(); } catch (Exception ignore) {}
	    } finally {
	        DBConnector.close(pstmt, conn);
	    }
	    return rowCount > 0;
	}
	
	
	
	// 알림을 삭제하는 메서드(아직 사용안함)
	public boolean notiDelete(long notiNum) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					DELETE FROM notifications
					WHERE noti_num = ?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setLong(1, notiNum);
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		// 작업의 성공 여부 (변화된 row 의 갯수로 판단)
		if (rowCount > 0) {
			return true; // 작업 성공
		} else {
			return false; // 작업 실패
		}
	}
	
	
	
	// 사용자의 안읽은 알림의 수를 반환하는 메서드
	public int notiReadCount(long usersNum) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int readCount = 0;
		
		try {
			conn = DBConnector.getConn();
			String sql = """
					SELECT COUNT(noti_read_code) AS noti_read_count
					FROM notifications
					WHERE noti_read_code = 10
						AND noti_recipient_num = ?
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, usersNum);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				readCount = rs.getInt("noti_read_count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return readCount;
	}
	
	
	
	// 알림을 읽음 처리하는 메서드
	public boolean notiSetRead(long notiNum) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					UPDATE notifications
					SET noti_read_code = 11
					WHERE noti_num = ?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setLong(1, notiNum);
			// sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
			rowCount = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		// 작업의 성공 여부 (변화된 row 의 갯수로 판단)
		if (rowCount > 0) {
			return true; // 작업 성공
		} else {
			return false; // 작업 실패
		}
	}
			
}
