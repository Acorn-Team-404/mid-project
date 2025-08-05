package model.faq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class FaqDao {
	private static FaqDao dao;
	static {
		dao=new FaqDao();
	}
	//생성자를 private 로 해서 외부에서 객체 생성하지 못하도록 
	private FaqDao() {}
	//자신의 참조값을 리턴해주는 static 메소드를 제공한다. 
	public static FaqDao getInstance() {
		return dao;
	}
	
	public List<FaqDto> getByCategory (String category){
		List<FaqDto> list=new ArrayList<>();
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT faq_question, faq_answer
				FROM faqs
				WHERE faq_category = ?
				ORDER BY faq_num ASC
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setString(1, category);
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			while (rs.next()) {
				FaqDto dto=new FaqDto();
				dto.setQuestion(rs.getString("faq_question"));
				dto.setAnswer(rs.getString("faq_answer"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
	
	public List<FaqDto> selectAll(){
		List<FaqDto> list=new ArrayList<>();
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT faq_question, faq_answer
				FROM faqs
				ORDER BY faq_num ASC
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			while (rs.next()) {
				FaqDto dto=new FaqDto();
				dto.setQuestion(rs.getString("faq_question"));
				dto.setAnswer(rs.getString("faq_answer"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return list;
	}
}