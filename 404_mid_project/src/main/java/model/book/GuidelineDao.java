package model.book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.util.DBConnector;

public class GuidelineDao {
	private static GuidelineDao dao;
	
	static {
		dao = new GuidelineDao();
	}
	
	private GuidelineDao() {
		
	}
	
	public static GuidelineDao getInstance() {
		return dao;
	}
	
	// Clob 를 String 변환해주는 메소드
	private String clobToString(Clob clob) {
		Reader reader = null;
		BufferedReader br = null;
		StringBuilder sb = null;
		
		try {
			reader = clob.getCharacterStream();
			br = new BufferedReader(reader);
			sb = new StringBuilder();
			String line;
			
			while((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
				if (reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 일단은 가이드번호로 고정된 가이드라인 가져오기
	public GuidelineDto getByGuideId(long guideId) {
		GuidelineDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = """
				SELECT *
				FROM GUIDELINES
				WHERE guide_id = ?
				""";
		
		try {
			conn = DBConnector.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, guideId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new GuidelineDto();
				dto.setGuideId(rs.getLong("guide_id"));
				dto.setGuideInformation(clobToString(rs.getClob("guide_information")));
				dto.setStayPolicy(clobToString(rs.getClob("guide_stay_policy")));
				dto.setRefundPolicy(clobToString(rs.getClob("guide_refund_policy")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return dto;
	}	
}
