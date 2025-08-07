package model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.util.DBConnector;

public class UserDao {

   private static UserDao dao;
   
   // static 초기화 블럭 (이클래스가 최초로 사용될때 한번 실행되는 블럭)
   static {
      //static 초기화 작업을 여기서 한다 (UserDao 객체를 생성해서 static 필드에 담는다)
      dao=new UserDao();
   }
   
   //외부에서 UserDao 객체를 생성하지 못하도록 생성자를 private 로 막는다.
   private UserDao() {}
   
   //UserDao 객체의 참조값을 리턴해주는 public static 메소드 제공
   public static UserDao getInstance() {
      //static 필드에 저장된 dao 의 참조값을 리턴해 준다. 
      return dao;
   }
   
   public UserDto getByUserNum(Long usersNum) {
       
	      UserDto dto=null;
	      //필요한 객체를 담을 지역변수를 미리 만든다 
	      Connection conn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	         conn = DBConnector.getConn();
	         //실행할 sql문
	         String sql = """
	            SELECT  users_id, users_name, users_pw, users_email, users_phone, TO_CHAR(users_birth, 'YYYY-MM-DD') AS users_birth, users_profile_image, users_role, users_updated_at, users_created_at
	            FROM users
	            WHERE users_num=?
	         """;
	         pstmt = conn.prepareStatement(sql);
	         //? 에 값 바인딩
	         pstmt.setLong(1, usersNum);
	         // select 문 실행하고 결과를 ResultSet 으로 받아온다
	         rs = pstmt.executeQuery();
	         //만일 select 되는 row 가 존재한다면
	         if(rs.next()) {
	            //UserDto 객체를 생성해서 
	            dto=new UserDto();
	            //select 된 정보를 담는다.
	            dto.setUsersId(rs.getString("users_id"));
	            dto.setUsersName(rs.getString("users_name"));
	            dto.setUsersPw(rs.getString("users_pw"));
	            dto.setUsersEmail(rs.getString("users_email"));
	            dto.setUsersPhone(rs.getString("users_phone"));
	            dto.setUsersBirth(rs.getString("users_birth"));
	            dto.setUsersProfileImage(rs.getString("users_profile_image"));
	   
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         DBConnector.close(rs, pstmt, conn);
	      }
	         return dto;       
	         
	    }
  
   //이메일 중복확인
   public boolean isEmailExist(String email) {
	    boolean result = false;
	    String sql = "SELECT users_email FROM users WHERE users_email = ?";
	    try (
	        Connection conn = new DBConnector().getConn();
	        PreparedStatement pstmt = conn.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, email);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            result = rs.next(); // 존재하면 true
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
   
   //비밀번호 재발급
   public boolean updateUserPassword(String userId, String hashed) {
      Connection conn = null;
      PreparedStatement pstmt = null;
      //변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
      int rowCount = 0;
      try {
         conn = DBConnector.getConn();
         String sql = """ 
               UPDATE users
               SET users_pw = ?
               WHERE users_id = ?
         """;
         pstmt = conn.prepareStatement(sql);
         // ? 에 순서대로 필요한 값 바인딩
         pstmt.setString(1, hashed);
         pstmt.setString(2, userId);

         
         // sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
         rowCount = pstmt.executeUpdate();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         DBConnector.close(pstmt, conn);
      }

      //변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아 낼수 있다.
      if (rowCount > 0) {
         return true; //작업 성공이라는 의미에서 true 리턴하기
      } else {
         return false; //작업 실패라는 의미에서 false 리턴하기
      }
   }
   
   
   
   
   
   //비밀번호 병경하려는 유저의 존재 확인
   public boolean findPassword(String usersName, String usersId, String usersEmail) {
      // 필요한 객체를 담을 지역변수를 미리 만든다.
      // ex) List<DTO> list=new ArrayList<>();
   
      boolean isUserValidForPwReset = false;
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      System.out.println(">> DAO 진입");
      System.out.println("입력값: " + usersName + ", " + usersId + ", " + usersEmail);
      
      try {
         conn = DBConnector.getConn();
         // 실행할 sql 문
         String sql = """
            SELECT users_Email FROM users 
            WHERE users_name=? AND users_email = ? AND users_id= ?
                     """;
         // ? 에 값 바인딩
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, usersName);
         pstmt.setString(2, usersEmail);
         pstmt.setString(3, usersId);

         // Select 문 실행하고 결과를 ResultSet 으로 받아온다
         rs = pstmt.executeQuery();
         // 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
         // 단일 : if  /  다중 : while
         
         if (rs.next()) {
            System.out.println("사용자 확인");
            isUserValidForPwReset = true;
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         DBConnector.close(rs, pstmt, conn);
      }
      return isUserValidForPwReset; //리턴할 객체;
   }
   
   
   
   
   //아이디 찾기
   public String findId(String usersName, String usersEmail) {
      // 필요한 객체를 담을 지역변수를 미리 만든다.
      // ex) List<DTO> list=new ArrayList<>();
      String usersId = null;
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         conn = DBConnector.getConn();
         // 실행할 sql 문
         String sql = """
            SELECT users_id FROM users 
            WHERE users_name=? AND users_email = ?
                     """;
         // ? 에 값 바인딩
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, usersName);
         pstmt.setString(2, usersEmail);

         // Select 문 실행하고 결과를 ResultSet 으로 받아온다
         rs = pstmt.executeQuery();
         // 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
         // 단일 : if  /  다중 : while
         
         if (rs.next()) {
            usersId = rs.getString("users_id");
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         DBConnector.close(rs, pstmt, conn);
      }
      return usersId; //리턴할 객체;
   }
   
   // 마이페이지 사용자 정보 조회
   public UserDto getUserById(String usersId) {
      UserDto dto = null;
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         conn = new DBConnector().getConn();
         String sql = """
               SELECT users_num, users_id, users_name, users_email, users_phone, 
               users_birth, users_profile_image, users_updated_at 
               FROM users 
               WHERE users_id = ?

               """;
         pstmt = conn.prepareStatement(sql);
         // ? 에 값 바인딩
         pstmt.setString(1, usersId);
         // select 문 실행하고 결과를 ResultSet으로 받아온
         rs = pstmt.executeQuery();
         // 반복문 돌면서 ResulteSet 에 담긴 데이터를 추출해서 리턴 해 줄 객체에 담는다
         while (rs.next()) {

         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if (conn != null)
               conn.close();
         } catch (Exception e) {
         }
      } return dto;
   }
   
   //회원정보 리턴
    public UserDto getByUserId(String usersId) {

       
      UserDto dto=null;
      //필요한 객체를 담을 지역변수를 미리 만든다 
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         conn = DBConnector.getConn();
         //실행할 sql문
         String sql = """
            SELECT users_num, users_id, users_name, users_pw, users_email, users_phone, TO_CHAR(users_birth, 'YYYY-MM-DD') AS users_birth, users_profile_image, users_role, users_updated_at, users_created_at
            FROM users
            WHERE users_id=?
         """;
         pstmt = conn.prepareStatement(sql);
         //? 에 값 바인딩
         pstmt.setString(1, usersId);
         // select 문 실행하고 결과를 ResultSet 으로 받아온다
         rs = pstmt.executeQuery();
         //만일 select 되는 row 가 존재한다면
         if(rs.next()) {
            //UserDto 객체를 생성해서 
            dto=new UserDto();
            //select 된 정보를 담는다.
            dto.setUsersNum(rs.getLong("users_num"));
            dto.setUsersId(rs.getString("users_id"));
            dto.setUsersName(rs.getString("users_name"));
            dto.setUsersPw(rs.getString("users_pw"));
            dto.setUsersEmail(rs.getString("users_email"));
            dto.setUsersPhone(rs.getString("users_phone"));
            dto.setUsersBirth(rs.getString("users_birth"));
            dto.setUsersRole(rs.getString("users_role"));
            dto.setUsersProfileImage(rs.getString("users_profile_image"));
   
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         DBConnector.close(rs, pstmt, conn);
      }
         return dto;       
         
    }
   
   
   //아이디 중복 체크
   public boolean isIdExist(String usersId) {//아이디 중복이면 true
      
      boolean isIdExist = false;
      
      Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
         conn = DBConnector.getConn();

         String sql = """
            SELECT users_id FROM users WHERE users_id=?
         """;
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, usersId);

         rs = pstmt.executeQuery();

         if(rs.next()) {
            isIdExist = true;
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         DBConnector.close(rs, pstmt, conn);
      }
      return isIdExist;
        
   }
   
   
   //회원가입
   public boolean insert(UserDto dto) {
      Connection conn = null;
      PreparedStatement pstmt = null;
      //변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
      int rowCount = 0;
      try {
         conn = DBConnector.getConn();
         String sql = """
            INSERT INTO users
            (users_num, users_id, users_name, users_pw, users_email, users_phone, users_birth, users_role, users_updated_at, users_created_at)
            VALUES(users_seq.NEXTVAL, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), DEFAULT, SYSDATE, SYSDATE)
         """;
         pstmt = conn.prepareStatement(sql);
         // ? 에 순서대로 필요한 값 바인딩
         pstmt.setString(1, dto.getUsersId());
         pstmt.setString(2, dto.getUsersName());
         pstmt.setString(3, dto.getUsersPw());
         pstmt.setString(4, dto.getUsersEmail());
         pstmt.setString(5, dto.getUsersPhone());
         pstmt.setString(6, dto.getUsersBirth());
         
         // sql 문 실행하고 변화된(추가된, 수정된, 삭제된) row 의 갯수 리턴받기
         rowCount = pstmt.executeUpdate();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         DBConnector.close(pstmt, conn);
      }

      //변화된 rowCount 값을 조사해서 작업의 성공 여부를 알아 낼수 있다.
      if (rowCount > 0) {
         return true; //작업 성공이라는 의미에서 true 리턴하기
      } else {
         return false; //작업 실패라는 의미에서 false 리턴하기
      }
   }
   
   	// 전화번호 수정
	public boolean updatePhone(UserDto user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					UPDATE users
					SET users_Phone = ?
					WHERE users_Id = ?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, user.getUsersPhone());
			pstmt.setString(2, user.getUsersId());
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
	
	// 회원 이름 수정
	public boolean updateName(UserDto user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					UPDATE users 
					SET users_Name  = ?
					WHERE users_Id = ?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, user.getUsersName());
			pstmt.setString(2, user.getUsersId());
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
	
	//회원 이름, 전화번호, 이메일만 조회
	public UserDto getBasicInfoByNum(long usersNum) {
		UserDto dto=null;
		//필요한 객체를 담을 지역변수를 미리 만든다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			//실행할 sql문
			String sql = """
				SELECT users_name, users_email, users_phone
				FROM users
				WHERE users_num=?
			""";
			pstmt = conn.prepareStatement(sql);
			//? 에 값 바인딩
			pstmt.setLong(1, usersNum);
			// select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			//반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 리턴해줄 객체에 담는다
			if (rs.next()) {
				dto=new UserDto();
				dto.setUsersName(rs.getString("users_name"));
				dto.setUsersEmail(rs.getString("users_email"));
				dto.setUsersPhone(rs.getString("users_phone"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}
		return dto;
	}
}
