package model.util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnector {
	// Connection 객체를 리턴해주는 정적 메소드
	// Connection conn = DBConnector.getConn(); 의 형태로 사용하면 됨

	public static Connection getConn() {
		Connection conn=null;
		try {
			//Tomcat 서버가 관리하는 Connection 객체를 하나 얻어와서 지역변수에 저장하는 작업하기
			Context initContext = new InitialContext();
			//Servers/context.xml 문서에 설정된 jdbc/myoracle 이라는 이름의 datasource 를 얻어온다.
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			//얻어온 datasource 객체를 이용해서 Connection 객체의 참조값을 얻어와서 지역변수에 저장
			DataSource ds = (DataSource)envContext.lookup("jdbc/midprj");
			conn = ds.getConnection();
			//예외가 발생하지 않고 여기까지 실행의 흐름이 넘어온다면 성공
			System.out.println("Team404 MidProject Connection 실행됨");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt, Connection conn) {
		try {
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
