// src/main/java/model/admin/StayInfoDao.java
package model.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.page.PageDto;
import model.room.RoomDto;
import model.util.DBConnector;

public class StayInfoDao {
    private static StayInfoDao dao;
    private StayInfoDao() {}
    public static StayInfoDao getInstance() {
        if (dao == null) dao = new StayInfoDao();
        return dao;
    }

    /**
     * STAY + ROOM 일괄 등록
     */
    public StayInfoDto insertStayWithRooms(StayInfoDto info) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnector.getConn();
            conn.setAutoCommit(false);

            // 1) STAY
            long staySeq = getSequence("stay_seq");
            info.setStayNum(staySeq);
            String staySql = """
                INSERT INTO stay (
                  stay_num, stay_user_num, stay_name, stay_addr,
                  stay_loc, stay_lat, stay_long, stay_phone,
                  stay_facilities, stay_update_at, stay_content
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)
            """;
            pstmt = conn.prepareStatement(staySql);
            pstmt.setLong(1, staySeq);
            pstmt.setInt(2, info.getStayUserNum());
            pstmt.setString(3, info.getStayName());
            pstmt.setString(4, info.getStayAddr());
            pstmt.setString(5, info.getStayLoc());
            pstmt.setString(6, info.getStayLat());
            pstmt.setString(7, info.getStayLong());
            pstmt.setString(8, info.getStayPhone());
            pstmt.setString(9, info.getStayFacilities());
            pstmt.setString(10, info.getStayContent());
            pstmt.executeUpdate();
            pstmt.close();

            // 2) ROOM
            List<RoomDto> rooms = info.getRooms();
            if (rooms != null) {
                String roomSql = """
                    INSERT INTO room (
                      room_num, room_stay_num, room_name, room_type,
                      room_price, room_adult_max, room_children_max,
                      room_infant_max, room_content, room_pax_max
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                for (RoomDto r : rooms) {
                    long roomSeq = getSequence("room_seq");
                    r.setRoomNum(roomSeq);
                    pstmt = conn.prepareStatement(roomSql);
                    pstmt.setLong(1, roomSeq);
                    pstmt.setLong(2, staySeq);
                    pstmt.setString(3, r.getRoomName());
                    pstmt.setString(4, r.getRoomType());
                    pstmt.setInt(5, r.getRoomPrice());
                    pstmt.setInt(6, r.getRoomAdultMax());
                    pstmt.setInt(7, r.getRoomChildrenMax());
                    pstmt.setInt(8, r.getRoomInfantMax());
                    pstmt.setString(9, r.getRoomContent());
                    pstmt.setInt(10, r.getRoomPaxMax());
                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        } finally {
            DBConnector.close(pstmt, conn);
        }

        return info;
    }

    /**
     * 시퀀스 NEXTVAL 반환 (seqName에는 "stay_seq", "room_seq" 등 입력)
     */
    private long getSequence(String seqName) {
        long val = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnector.getConn();
            String sql = "SELECT " + seqName + ".NEXTVAL AS num FROM dual";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                val = rs.getLong("num");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }
        return val;
    }
    
    /**
     * 전체 숙소 목록 조회
     */
    public List<StayInfoDto> getList() {
        List<StayInfoDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnector.getConn();
            String sql = """
                SELECT stay_num, stay_name
                FROM stay
                ORDER BY stay_num DESC
            """;
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                StayInfoDto dto = new StayInfoDto();
                dto.setStayNum(rs.getLong("stay_num"));
                dto.setStayName(rs.getString("stay_name"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnector.close(rs, pstmt, conn);
        }
        return list;
    }
    
    /**
     * STAY + min(room_price) + avg(review_rating) + 대표이미지 1건
     */
    public List<StaySummaryDto> getStaySummaries() {
        List<StaySummaryDto> list = new ArrayList<>();
        String sql = """
				SELECT 
				  s.stay_num,
				  s.stay_name,
				  s.stay_loc,
				  NVL((SELECT MIN(r.room_price)
				         FROM room r
				        WHERE r.room_stay_num   = s.stay_num), 0)                 AS min_price,
				  NVL((SELECT ROUND(AVG(rv.review_rating), 2)
				         FROM review rv
				        WHERE rv.review_stay_num = s.stay_num), 0)                 AS avg_rating,
				  NVL((SELECT COUNT(*)
				         FROM review rv
				        WHERE rv.review_stay_num = s.stay_num), 0)                 AS review_count,
				  NVL((SELECT img.image_saved_name
				         FROM image_file img
				        WHERE img.image_target_type = 'stay'
				          AND img.image_target_id   = s.stay_num
				          AND ROWNUM = 1), 'default.jpg')                           AS image_name,
				  NVL((SELECT MAX(p.page_num)
				         FROM page p
				        WHERE p.page_stay_num    = s.stay_num), 0)                 AS page_num
				FROM stay s
				WHERE s.stay_delete = 'N'
				ORDER BY s.stay_num DESC
                """;
            try (
              Connection conn = DBConnector.getConn();
              PreparedStatement pstmt = conn.prepareStatement(sql);
              ResultSet rs = pstmt.executeQuery();
            ) {
              while (rs.next()) {
                StaySummaryDto dto = new StaySummaryDto();
                dto.setStayNum     (rs.getLong  ("stay_num"));
                dto.setStayName    (rs.getString("stay_name"));
                dto.setStayLoc     (rs.getString("stay_loc"));
                dto.setMinPrice    (rs.getInt   ("min_price"));
                dto.setAvgRating   (rs.getDouble("avg_rating"));
                dto.setReviewCount (rs.getInt   ("review_count"));  // ★ 여기
                dto.setImageName   (rs.getString("image_name"));
                dto.setLatestPageNum(rs.getLong("page_num"));
                list.add(dto);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            return list;
        }
    
    public Long getPageNumByStayNum(long stayNum) {
        Long pageNum = 0L;  // 기본값을 0으로 설정

        String sql = """
            SELECT NVL(MAX(p.page_num), 0) AS page_num
              FROM page p
             WHERE p.page_stay_num = ?
            """;

        try (
            Connection conn = DBConnector.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            // 1) ?에 stayNum 바인딩
            pstmt.setLong(1, stayNum);

            // 2) 쿼리 실행
            try (ResultSet rs = pstmt.executeQuery()) {
                // 3) 결과에서 page_num 읽기
                if (rs.next()) {
                    pageNum = rs.getLong("page_num");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pageNum;
    }
    
	// 글 하나의 정보 불러오기
	public StayInfoDto getByNum(long stayNum) {
		StayInfoDto dto=null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.getConn();
			// 실행할 sql 문
			String sql = """
				SELECT stay_num, stay_name, stay_loc, stay_content, stay_addr
				FROM stay
				WHERE stay_num=?
			""";
			pstmt = conn.prepareStatement(sql);
			// ? 에 값 바인딩
			pstmt.setLong(1, stayNum);
			// Select 문 실행하고 결과를 ResultSet 으로 받아온다
			rs = pstmt.executeQuery();
			// 반복문 돌면서 ResultSet 에 담긴 데이터를 추출해서 어떤 객체에 담는다
			// 단일 : if  /  다중 : while
			if (rs.next()) {
				dto=new StayInfoDto();
				dto.setStayNum(rs.getLong("stay_num"));
				dto.setStayName(rs.getString("stay_name"));
				dto.setStayLoc(rs.getString("stay_loc"));
				dto.setStayContent(rs.getString("stay_content"));
				dto.setStayAddr(rs.getString("stay_addr"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		} // 하단에 return 값 넣어주셔야함!
		return dto;
	}
}
