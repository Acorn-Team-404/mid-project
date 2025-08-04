<%@page import="model.image.ImageDto"%>
<%@page import="model.room.RoomDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.room.RoomDto" %>
<%@ page import="model.image.ImageDao" %>

<%
  int stayNum = 100; // 실제론 request.getParameter("stayNum") 등으로 받기
  List<RoomDto> roomList = RoomDao.getInstance().getRoomListByStayNum(stayNum);
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>객실 목록</title>
  <jsp:include page="/WEB-INF/include/resource.jsp"/>
  <style>
    .carousel-img {
      object-fit: cover;
      width: 100%;
      height: 100%;
    }
    .carousel-box {
      flex: 0 0 360px;
      aspect-ratio: 3 / 2;
      overflow: hidden;
    }
  </style>
</head>
<body>
  <div class="container my-5">
    <h3 class="fw-bold mb-4">객실 선택</h3>

    <% for (RoomDto room : roomList) {
         List<ImageDto> imageList = ImageDao.getInstance().getListByTargetLong("room", room.getRoomNum());
         String carouselId = "carousel-" + room.getRoomNum();
    %>
    <div class="card d-flex flex-row shadow-sm mb-4 overflow-hidden" style="border-radius: 12px;">
      
      <!-- 캐러셀 영역 -->
      <div class="carousel-box">
        <% if (imageList != null && !imageList.isEmpty()) { %>
        <div id="<%= carouselId %>" class="carousel slide h-100" data-bs-ride="carousel">
          <div class="carousel-inner h-100">
            <% for (int j = 0; j < imageList.size(); j++) {
                 String savedName = imageList.get(j).getImageSavedName();
            %>
            <div class="carousel-item <%= (j == 0 ? "active" : "") %> h-100">
              <img src="<%= request.getContextPath() %>/show.img?imageName=<%= savedName %>"
                   class="carousel-img"
                   alt="객실 이미지 <%= j+1 %>">
            </div>
            <% } %>
          </div>
          <% if (imageList.size() > 1) { %>
          <button class="carousel-control-prev" type="button" data-bs-target="#<%= carouselId %>" data-bs-slide="prev">
            <span class="carousel-control-prev-icon"></span>
          </button>
          <button class="carousel-control-next" type="button" data-bs-target="#<%= carouselId %>" data-bs-slide="next">
            <span class="carousel-control-next-icon"></span>
          </button>
          <% } %>
        </div>
        <% } else { %>
        <img src="<%= request.getContextPath() %>/images/no-image.png"
             class="carousel-img" alt="기본 이미지">
        <% } %>
      </div>

      <!-- 객실 정보 -->
      <div class="p-3 d-flex flex-column justify-content-between flex-grow-1">
        <div>
          <h5 class="fw-bold mb-1"><%= room.getRoomName() %></h5>
          <div class="text-muted small">타입: <%= room.getRoomType() %> / 최대 <%= room.getRoomPaxMax() %>명</div>
        </div>
        <div class="mt-2 d-flex justify-content-between align-items-end">
          <div>
            <span class="fw-bold text-primary">₩<%= room.getRoomPrice() %></span> <span class="text-muted small">/ 박</span>
          </div>
          <button class="btn btn-dark"
                  onclick="selectRoom('<%= room.getRoomNum() %>', '<%= room.getRoomName() %>', <%= room.getRoomPrice() %>)">
            객실 선택
          </button>
        </div>
      </div>
    </div>
    <% } %>
  </div>

  <script>
    function selectRoom(roomNum, roomName, roomPrice) {
      alert("선택된 객실: " + roomName + " (가격: ₩" + roomPrice + ")");
    }
  </script>
</body>
</html>
