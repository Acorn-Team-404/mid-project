package controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.admin.StayInfoDto;
import model.admin.StayInfoDao;
import model.room.RoomDto;

@WebServlet("/saveStay")
@MultipartConfig
public class SaveStayServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    // 1) DTO에 텍스트 파라미터 채우기
    StayInfoDto info = new StayInfoDto();
    HttpSession session = req.getSession(false);
    Object userNumObj = session!=null ? session.getAttribute("usersNum") : null;
    info.setStayUserNum(userNumObj!=null 
        ? Integer.parseInt(userNumObj.toString()) 
        : 0);
    info.setStayName(req.getParameter("stay_name"));
    info.setStayLoc(req.getParameter("stay_region"));
    info.setStayAddr(req.getParameter("stay_address"));
    info.setStayLat(req.getParameter("stay_lat"));
    info.setStayLong(req.getParameter("stay_lng"));
    info.setStayPhone(req.getParameter("stay_contact"));
    info.setStayFacilities(req.getParameter("stay_facilities"));

    // 2) 객실 DTO 목록 파싱
    List<RoomDto> rooms = new ArrayList<>();
    for (int i = 0; ; i++) {
      String p = "rooms[" + i + "]";
      if (req.getParameter(p + ".roomName")==null) break;
      RoomDto r = new RoomDto();
      r.setRoomName(req.getParameter(p + ".roomName"));
      r.setRoomType(req.getParameter(p + ".roomType"));
      String price = req.getParameter(p + ".roomPrice").replace(",", "");
      r.setRoomPrice(Integer.parseInt(price));
      r.setRoomAdultMax(Integer.parseInt(req.getParameter(p + ".roomAdultMax")));
      r.setRoomChildrenMax(Integer.parseInt(req.getParameter(p + ".roomChildrenMax")));
      r.setRoomInfantMax(Integer.parseInt(req.getParameter(p + ".roomInfantMax")));
      r.setRoomPaxMax(Integer.parseInt(req.getParameter(p + ".roomPaxMax")));
      r.setRoomContent(req.getParameter(p + ".roomContent"));
      rooms.add(r);
    }
    info.setRooms(rooms);

    // 3) DB 저장 (stayNum, roomNum까지 info에 채워져 반환)
    info = StayInfoDao.getInstance().insertStayWithRooms(info);

    if (info.getStayNum()==0) {
      resp.setContentType("text/html; charset=UTF-8");
      resp.getWriter().println(
        "<script>alert('숙소 저장 실패'); history.back();</script>"
      );
      return;
    }

    // 4) 이미지 업로드(include 방식)  
    //    form의 file-input을 모두 name="uploadFile"로 통일하세요.
    req.getRequestDispatcher("/stay.img?target_id="+info.getStayNum())
       .include(req, resp);
    for (RoomDto r: info.getRooms()) {
      req.getRequestDispatcher("/room.img?target_id="+r.getRoomNum())
         .include(req, resp);
    }

    // 5) 완료 후 목록 페이지 이동
    resp.setContentType("text/html; charset=UTF-8");
    resp.getWriter().println(
      "<script>alert('숙소 및 이미지 저장 완료');"
    + "location.href='${pageContext.request.contextPath}/test/stay-list.jsp';</script>"
    );
  }
}
