package model.image;

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
    
    // Sort 자동화를 이용한 insert 메소드
    public boolean insertAutoSort(String originalName, String savedName, String targetType, int targetId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int rowCount = 0;

        try {
            conn = DBConnector.getConn();

            // 1. 현재 최대 sort_order 조회
            String maxSql = """
                SELECT NVL(MAX(image_sort_order), 0) + 1
                FROM image_file
                WHERE image_target_type = ? AND image_target_id = ?
            """;
            pstmt = conn.prepareStatement(maxSql);
            pstmt.setString(1, targetType);
            pstmt.setInt(2, targetId);
            rs = pstmt.executeQuery();

            int nextSortOrder = 1;
            if (rs.next()) {
                nextSortOrder = rs.getInt(1);
            }

            // pstmt, rs 재사용 안 되므로 닫고 새로 만듦
            DBConnector.close(rs, pstmt, null);

            // 2. INSERT 쿼리
            String insertSql = """
                INSERT INTO image_file (
                    image_num, image_original_name, image_saved_name,
                    image_target_type, image_target_id, image_sort_order
                )
                VALUES (image_file_seq.NEXTVAL, ?, ?, ?, ?, ?)
            """;

            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, originalName);
            pstmt.setString(2, savedName);
            pstmt.setString(3, targetType);
            pstmt.setInt(4, targetId);
            pstmt.setInt(5, nextSortOrder);

            rowCount = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(pstmt, conn); // rs는 위에서 이미 닫음
        }

        return rowCount > 0; // true/false 반환
    }
    
    // 내부 공통 메소드: long 타입 targetId 로 통일
    private List<ImageDto> getListByTargetInternal(String targetType, long targetId) {
        List<ImageDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConn();
            String sql = """
                SELECT image_num, image_original_name, image_saved_name,
                       TO_CHAR(image_upload_date, 'YYYY-MM-DD') AS image_upload_date,
                       image_sort_order, image_target_type, image_target_id
		               FROM image_file
		               WHERE image_target_type = ? AND image_target_id = ?
		               ORDER BY image_sort_order ASC
            """;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, targetType);
            pstmt.setLong(2, targetId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ImageDto dto = new ImageDto();
                dto.setImageNum(rs.getInt("image_num"));
                dto.setImageOriginalName(rs.getString("image_original_name"));
                dto.setImageSavedName(rs.getString("image_saved_name"));
                dto.setImageUploadDate(rs.getString("image_upload_date"));
                dto.setImageSortOrder(rs.getInt("image_sort_order"));
                dto.setImageTargetType(rs.getString("image_target_type"));
                dto.setImageTargetId(rs.getInt("image_target_id"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }

        return list;
    }

    // int 버전
    public List<ImageDto> getListByTarget(String targetType, int targetId) {
        return getListByTargetInternal(targetType, (long) targetId);
    }

    // long 버전
    public List<ImageDto> getListByTargetLong(String targetType, long targetId) {
        return getListByTargetInternal(targetType, targetId);
    }

    
    
    // userDao 프로필 이미지 업데이트 대체 메소드
    public boolean updateUserProfileImage(int userNum, String savedName) {
        Connection conn = null;
		PreparedStatement pstmt = null;
		// 변화된 row 의 갯수를 담을 변수 선언하고 0으로 초기화
		int rowCount = 0;
		try {
			conn = DBConnector.getConn();
			String sql = """
					UPDATE users 
					SET users_profile_image = ?, users_updated_at = SYSDATE
					WHERE users_num = ?
					""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 순서대로 필요한 값 바인딩
			pstmt.setString(1, savedName);
			pstmt.setInt(2, userNum);
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
    
    // 내부 공통 메소드
    private ImageDto selectSingleImage(String targetType, long targetId) {
        ImageDto dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnector.getConn();
            String sql = """
                SELECT image_num, image_original_name, image_saved_name,
                       TO_CHAR(image_upload_date, 'YYYY-MM-DD') AS image_upload_date,
                       image_sort_order, image_target_type, image_target_id
		               FROM image_file
		               WHERE image_target_type = ?
			               AND image_target_id = ?
			               AND image_sort_order = 1
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, targetType);
            pstmt.setLong(2, targetId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new ImageDto();
                dto.setImageNum(rs.getInt("image_num"));
                dto.setImageOriginalName(rs.getString("image_original_name"));
                dto.setImageSavedName(rs.getString("image_saved_name"));
                dto.setImageUploadDate(rs.getString("image_upload_date"));
                dto.setImageSortOrder(rs.getInt("image_sort_order"));
                dto.setImageTargetType(rs.getString("image_target_type"));
                dto.setImageTargetId(rs.getInt("image_target_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }
        return dto;
    }

    // int 버전
    public ImageDto selectByIntSingleImage(String targetType, int targetId) {
        return selectSingleImage(targetType, (long) targetId);
    }

    // long 버전
    public ImageDto selectByLongSingleImage(String targetType, long targetId) {
        return selectSingleImage(targetType, targetId);
    }
    
    // target 에 따라 시퀀스 가져오는 메소드
    public int getSequence(String targetType) {
        int num = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConn();

            // 💡 시퀀스명 결정
            String seqName = null;
            if ("post".equals(targetType)) {
                seqName = "posts_seq";
            } else if ("room".equals(targetType)) {
                seqName = "room_seq";
            } else if ("index".equals(targetType)) {
                seqName = "index_seq";
            } else {
                throw new IllegalArgumentException("Invalid targetType: " + targetType);
            }

            String sql = "SELECT " + seqName + ".NEXTVAL AS num FROM dual";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                num = rs.getInt("num");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }

        return num;
    }

}
