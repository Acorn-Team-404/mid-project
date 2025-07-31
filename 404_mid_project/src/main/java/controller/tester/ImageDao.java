package controller.tester;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.util.DBConnector;

public class ImageDao {
	private static ImageDao dao;
	private ImageDao() {}

    public static ImageDao getInstance() {
        if (dao == null) dao = new ImageDao();
        return dao;
    }
 
    // 업로드된 이미지 DB에 저장 
    public boolean insert(String fileName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowCount = 0;
        try {
            conn = DBConnector.getConn();
            String sql = "INSERT INTO nas_images (id, file_name) VALUES (nas_images_seq.NEXTVAL, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fileName);
            rowCount = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return rowCount > 0;
    } 

    // 이미지 목록 가져오기
    public List<ImageDto> getImageList() {
        List<ImageDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnector.getConn();
            String sql = "SELECT id, file_name, TO_CHAR(created_at, 'YYYY-MM-DD HH24:MI:SS') AS created_at FROM nas_images ORDER BY id DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ImageDto dto = new ImageDto();
                dto.setId(rs.getInt("id"));
                dto.setFileName(rs.getString("file_name"));
                dto.setCreatedAt(rs.getString("created_at"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return list;
    }
}
