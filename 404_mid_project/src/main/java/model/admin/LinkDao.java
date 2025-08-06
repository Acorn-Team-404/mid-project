package model.admin;

import java.sql.*;
import java.util.*;

import model.util.DBConnector;

public class LinkDao {
    // DAO 인스턴스
    private static LinkDao dao;
    static {
        dao = new LinkDao();
    }
    private LinkDao() { }
    public static LinkDao getInstance() {
        return dao;
    }

    /**
     * 모든 링크를 link_num 순서대로 조회
     */
    public List<LinkDto> getList() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<LinkDto> list = new ArrayList<>();

        try {
            conn = DBConnector.getConn();
            String sql = "SELECT link_num, link_title, link_url, link_action "
                       + "FROM link "
                       + "ORDER BY link_num";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkDto dto = new LinkDto();
                dto.setLinkNum(rs.getInt("link_num"));
                dto.setLinkTitle(rs.getString("link_title"));
                dto.setLinkUrl(rs.getString("link_url"));
                dto.setLinkAction(rs.getString("link_action"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ignored) {}
        }
        return list;
    }
}
