package model.noti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
						WHERE noti_read_code = 10) AS noti_read_count,
						b.book_num AS noti_book_num,
					    TO_CHAR(b.book_checkin_date, 'YYYY-MM-DD') AS noti_book_checkin,
					    TO_CHAR(b.book_checkout_date, 'YYYY-MM-DD') AS noti_book_checkout,
					    s.stay_num AS noti_stay_num,
					    s.stay_name AS noti_stay_name,
					    comm.comment_content AS noti_comment_content,
					    comm.comment_parent_num AS noti_comment_parent_num,
					    u.users_num AS noti_comment_users_num,
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
					 AND n.noti_sender_num = comm.comment_writer
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
					       AND img.image_target_id = s.stay_num)
					   OR (n.noti_image_type = 'comment'
					       AND img.image_target_id = u.users_num)
					    )
					LEFT JOIN common_code c
					  ON c.cc_group_id = 'NOTI_TYPE'
					 AND c.cc_code = n.noti_type_code
					WHERE n.noti_recipient_num = ?
						AND n.noti_num > ?
					ORDER BY n.noti_num ASC;
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, usersNum);
			pstmt.setLong(2, lastNotiNum);
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
	
	
	
	// 전체 알림 목록을 반환하는 메서드
	public List<NotificationDto> notiSelectByUsersNum(long usersNum) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<NotificationDto> list = new ArrayList<>(); // row를 담을 빈 배열

		try {
			conn = DBConnector.getConn();
			String sql = """
					SELECT noti_num, NVL(noti_sender_num, 0) AS noti_sender_num,
							(SELECT cc_code_name
							FROM common_code
							WHERE cc_group_id = 'NOTI_TYPE'
							AND cc_code = 10) noti_type,
							noti_message, noti_read_code, noti_created_at,
							TRUNC(SYSDATE - noti_created_at) AS noti_days_ago
					FROM notifications
					WHERE noti_recipient_num = 14
					ORDER BY noti_num DESC
					""";
			pstmt = conn.prepareStatement(sql);
			//pstmt.setLong(1, usersNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				NotificationDto dto = new NotificationDto();
				dto.setNotiNum(rs.getLong("noti_num"));
				dto.setNotiSenderNum(rs.getLong("noti_sender_num"));
				dto.setNotiType(rs.getString("noti_type"));
				dto.setNotiMessage(rs.getString("noti_message"));
				dto.setNotiReadCode(rs.getInt("noti_read_code"));
				dto.setNotiCreatedAt(rs.getString("noti_created_at"));
				dto.setNotiDaysAgo(rs.getString("noti_days_ago"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
	
	
	
	
	// 댓글 작성 시 함께 실행될 댓글알림 insert 메서드
	public boolean notiInsert(NotificationDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		int rowCount = 0;

		try {
			conn = DBConnector.getConn();
			String sql = """
					INSERT INTO notifications (
						noti_num, noti_recipient_num, noti_sender_num, noti_type_code,
						noti_target_type_code, noti_target_num, noti_message
					)
					VALUES(noti_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)
					""";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getNotiRecipientNum());
			pstmt.setLong(2, dto.getNotiSenderNum());
			pstmt.setInt(3, dto.getNotiTypeCode());
			pstmt.setInt(4, dto.getNotiTargetTypeCode());
			pstmt.setString(5, dto.getNotiTargetNum());
			pstmt.setString(6, dto.getNotiMessage());

			rowCount = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(pstmt, conn);
		}
		if (rowCount > 0) {
			System.out.println(rowCount + "개 작업 성공");
			return true;
		} else {
			System.out.println("작업 실패");
			return false;
		}
	}
	
	
	
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
