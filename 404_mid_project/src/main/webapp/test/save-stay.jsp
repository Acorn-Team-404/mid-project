<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="true"
         import="
           java.util.List,
           java.util.ArrayList,
           model.admin.StayInfoDto,
           model.admin.StayInfoDao,
           model.room.RoomDto
         " %>
<%
    // 1) 문자 인코딩
    request.setCharacterEncoding("UTF-8");

    // 2) StayInfoDto에 폼값 담기
    StayInfoDto info = new StayInfoDto();
    Object userNumObj = session.getAttribute("usersNum");
    info.setStayUserNum(userNumObj!=null
        ? Integer.parseInt(userNumObj.toString())
        : 0);
    info.setStayName(request.getParameter("stay_name"));
    info.setStayLoc(request.getParameter("stay_region"));
    info.setStayAddr(request.getParameter("stay_address"));
    info.setStayLat(request.getParameter("stay_lat"));
    info.setStayLong(request.getParameter("stay_lng"));
    info.setStayPhone(request.getParameter("stay_contact"));
    info.setStayFacilities(request.getParameter("stay_facilities"));

    // 3) 객실 정보 파싱
    List<RoomDto> roomList = new ArrayList<>();
    for (int i = 0; ; i++) {
        String prefix = "rooms[" + i + "]";
        if (request.getParameter(prefix + ".roomName") == null) break;
        RoomDto r = new RoomDto();
        r.setRoomName(request.getParameter(prefix + ".roomName"));
        r.setRoomType(request.getParameter(prefix + ".roomType"));
        String price = request.getParameter(prefix + ".roomPrice").replace(",", "");
        r.setRoomPrice(Integer.parseInt(price));
        r.setRoomAdultMax(Integer.parseInt(request.getParameter(prefix + ".roomAdultMax")));
        r.setRoomChildrenMax(Integer.parseInt(request.getParameter(prefix + ".roomChildrenMax")));
        r.setRoomInfantMax(Integer.parseInt(request.getParameter(prefix + ".roomInfantMax")));
        r.setRoomPaxMax(Integer.parseInt(request.getParameter(prefix + ".roomPaxMax")));
        r.setRoomContent(request.getParameter(prefix + ".roomContent"));
        roomList.add(r);
    }
    info.setRooms(roomList);

    // 4) DAO 호출: stayNum과 각 roomNum이 info에 채워져서 돌아옵니다
    info = StayInfoDao.getInstance().insertStayWithRooms(info);
    if (info.getStayNum() == 0) {
        out.println("<script>alert('숙소 저장에 실패했습니다.'); history.back();</script>");
        return;
    }

    // 5) 이미지 업로드 서블릿(include) 호출
    // — 모든 file-input name="uploadFile"로 통일 필요
    RequestDispatcher rdStay = request.getRequestDispatcher(
        "/stay.img?target_id=" + info.getStayNum()
    );
    rdStay.include(request, response);

    for (RoomDto r : info.getRooms()) {
        RequestDispatcher rdRoom = request.getRequestDispatcher(
            "/room.img?target_id=" + r.getRoomNum()
        );
        rdRoom.include(request, response);
    }

    // 6) 완료 리다이렉트
    out.println(
      "<script>alert('숙소 및 이미지 저장 완료');"
    + "location.href='stay-list.jsp';</script>"
    );
%>
