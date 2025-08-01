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
						noti_message, noti_read_code, noti_created_at
						TRUNC(SYSDATE - noti_created_at) AS noti_days_ago
					FROM notifications
					WHERE noti_recipient_num = ?
					ORDER BY noti_num DESC
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, usersNum);
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
	public boolean commentInsert(NotificationDto dto) {
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
			pstmt.setLong(5, dto.getNotiTargetNum());
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
	
	
	
	public boolean NotiGetByDelete(long notiNum) {
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
	
			
}
