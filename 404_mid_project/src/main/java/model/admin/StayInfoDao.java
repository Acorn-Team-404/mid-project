// src/main/java/model/admin/StayInfoDao.java
package model.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
                  stay_facilities
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
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
}
