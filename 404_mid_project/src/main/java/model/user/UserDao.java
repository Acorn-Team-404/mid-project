package model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.util.DBConnector;

public class UserDao {

    private static UserDao dao;

    // static 초기화 블럭 (이 클래스가 최초로 사용될 때 한 번 실행되는 블럭)
    static {
        dao = new UserDao();
    }

    // 외부에서 UserDao 객체를 생성하지 못하도록 생성자를 private로 막는다.
    private UserDao() {}

    // UserDao 객체의 참조값을 리턴해주는 public static 메소드 제공
    public static UserDao getInstance() {
        return dao;
    }

    // 아이디 찾기
    public String findId(String usersName, String usersEmail) {
        String usersId = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnector.getConn();
            String sql = """
                SELECT users_id FROM users 
                WHERE users_name = ? AND users_email = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usersName);
            pstmt.setString(2, usersEmail);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                usersId = rs.getString("users_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }
        return usersId;
    }

    // 마이페이지 사용자 정보 조회
    public UserDto getUserById(String usersId) {
        UserDto dto = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnector.getConn();
            String sql = """
                SELECT users_num, users_id, users_name, users_email, users_phone, 
                       users_birth, users_profile_image, users_updated_at 
                FROM users 
                WHERE users_id = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usersId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new UserDto();
                dto.setNum(rs.getLong("users_num"));
                dto.setUsersId(rs.getString("users_id"));
                dto.setUsersName(rs.getString("users_name"));
                dto.setUsersEmail(rs.getString("users_email"));
                dto.setUsersPhone(rs.getString("users_phone"));
                dto.setUsersBirth(rs.getString("users_birth"));
                dto.setUsersProfileImage(rs.getString("users_profile_image"));
                dto.setUsersUpdatedAt(rs.getString("users_updated_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }
        return dto;
    }

    // 회원정보 리턴
    public UserDto getByUserId(String usersId) {
        UserDto dto = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnector.getConn();
            String sql = """
                SELECT users_num, users_id, users_name, users_pw, users_email, 
                       users_phone, users_birth, users_profile_image, 
                       users_role, users_updated_at, users_created_at
                FROM users
                WHERE users_id = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usersId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new UserDto();
                dto.setNum(rs.getLong("users_num"));
                dto.setUsersId(rs.getString("users_id"));
                dto.setUsersName(rs.getString("users_name"));
                dto.setUsersPw(rs.getString("users_pw"));
                dto.setUsersEmail(rs.getString("users_email"));
                dto.setUsersPhone(rs.getString("users_phone"));
                dto.setUsersBirth(rs.getString("users_birth"));
                dto.setUsersProfileImage(rs.getString("users_profile_image"));
                dto.setUsersRole(rs.getString("users_role"));
                dto.setUsersUpdatedAt(rs.getString("users_updated_at"));
                dto.setUsersCreatedAt(rs.getString("users_created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }
        return dto;
    }

    // 아이디 중복 체크
    public boolean isIdExist(String usersId) {
        boolean isIdExist = false;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConn();
            String sql = """
                SELECT users_id FROM users WHERE users_id = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usersId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                isIdExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }

        return isIdExist;
    }

    // 회원가입
    public boolean insert(UserDto dto) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowCount = 0;

        try {
            conn = DBConnector.getConn();
            String sql = """
                INSERT INTO users
                (users_num, users_id, users_name, users_pw, users_email, users_phone, 
                 users_birth, users_profile_image, users_role, users_updated_at, users_created_at)
                VALUES(users_seq.NEXTVAL, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, DEFAULT, SYSDATE, SYSDATE)
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getUsersId());
            pstmt.setString(2, dto.getUsersName());
            pstmt.setString(3, dto.getUsersPw());
            pstmt.setString(4, dto.getUsersEmail());
            pstmt.setString(5, dto.getUsersPhone());
            pstmt.setString(6, dto.getUsersBirth());

            rowCount = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(pstmt, conn);
        }

        return rowCount > 0;
    }
}