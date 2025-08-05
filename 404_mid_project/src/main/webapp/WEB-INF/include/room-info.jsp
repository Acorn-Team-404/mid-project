<%@page import="model.image.ImageDto"%>
<%@page import="model.room.RoomDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.room.RoomDto" %>
<%@ page import="model.image.ImageDao" %>

<%
  // 예시: 현재 숙소 번호를 파라미터로 받는다고 가정
  //int stayNum = Integer.parseInt(request.getParameter("stayNum"));
  int stayNum = 100;
  List<RoomDto> roomList = RoomDao.getInstance().getRoomListByStayNum(stayNum);
  
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>객실 목록</title>
  <jsp:include page="/WEB-INF/include/resource.jsp"/>
</head>
<body>
  <div class="container my-5">
    <div class="row">
      <% for (RoomDto room : roomList) {
					List<ImageDto> imageList = ImageDao.getInstance().getListByTargetLong("room", room.getRoomNum());
          String carouselId = "carousel-room-" + room.getRoomNum();
      %>
      <div class="col-md-7">
        <div class="card mb-4 shadow-sm">
          <div class="row g-0">
          		<!--  최상단 숙소명: -->
<%-- 		        <div class="mb-2">
			        <label class="form-label" hidden>숙소명!</label>
			        <p class="room-type"><%= room.getStayName() %> 숙소명</p>
			        <input type="hidden" name="roomType" id="roomType" value="<%= room.getRoomType() %>">
		    	</div> --%>
            
           <!-- ✅ 캐러셀 이미지 영역 -->
           <div class="col-md-5">
             <% if (imageList != null && !imageList.isEmpty()) { %>
             <!-- ✅ (1) 캐러셀 컨테이너에 h-100 추가: 이미지 영역 전체 높이를 꽉 채움 -->
             <div id="<%= carouselId %>" class="carousel slide h-100" data-bs-ride="carousel">
         	    <!-- ✅ (2) 비율 유지 클래스 ratio-4x3 + 높이 유지 h-100 -->
               <div class="carousel-inner h-100 ratio ratio-4x3 rounded overflow-hidden">
                 <% for (int j = 0; j < imageList.size(); j++) { 
               		  String savedName = imageList.get(j).getImageSavedName();
                 %>
                 <div class="carousel-item <%= (j == 0) ? "active" : "" %>">
                 		<!-- ✅ (3) object-fit-cover: 비율 유지하며 꽉 채움, 이미지 찌그러짐 방지 -->
      								<!-- ✅ h-100: 부모 비율에 맞춰 세로도 100% 채움 -->
                    <img src="<%= request.getContextPath() %>/show.img?imageName=<%= savedName %>"
								         class="d-block w-100 h-100 object-fit-cover"
								         alt="객실 이미지 <%= j + 1 %>">
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
             	<!-- ✅ (4) 이미지가 없을 때도 동일한 비율 유지: ratio-4x3로 래핑 -->
								<div class="ratio ratio-4x3">
              	 <img src="<%= request.getContextPath() %>/images/no-image.png"
               	     class="img-fluid h-100 w-100" alt="기본 이미지">
            		</div>
             <% } %>
           </div>

            <!-- ✅ 객실 정보 -->
            <div class="col-md-7 p-3 d-flex flex-column justify-content-between">
              <div>
                <h5 class="fw-bold mb-2"><%= room.getRoomName() %></h5>
                <p class="mb-1">숙소명: <%= room.getRoomStayName() %></p>
                <p class="mb-1">타입: <%= room.getRoomType() %></p>
                <p class="mb-1">최대 인원: <%= room.getRoomPaxMax() %>명</p>
                <p class="mb-1">가격: ₩<%= room.getRoomPrice() %></p>
              </div>
              <div class="text-end">
              	<!--  객실 상세보기 버튼 (모달창 띄우기) -->
                <button class="btn btn-dark btn-room-select"
					data-bs-toggle="modal" data-bs-target="#roomModal<%= room.getRoomNum() %>">
					객실 상세보기
                </button>
              </div>
            </div>
            
        	<!-- 객실 상세정보 보기 영역 (모달처럼 펼치기) -->
        	<div class="modal fade" id="roomModal<%= room.getRoomNum() %>" tabindex="-1" aria-labelledby="roomModalLabel<%= room.getRoomNum() %>" aria-hidden="true">
			  <div class="modal-dialog modal-lg modal-dialog-centered">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="roomModalLabel<%= room.getRoomNum() %>"><%= room.getRoomName() %> - 객실 상세정보</h5>
			        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
			      </div>
			      <div class="modal-body">
			        <div class="row">
			          <div class="col-md-6">
			            <%-- 첫 번째 이미지 보여주기 --%>
			            <% if (imageList != null && !imageList.isEmpty()) { %>
			              <img src="<%= request.getContextPath() %>/show.img?imageName=<%= imageList.get(0).getImageSavedName() %>" class="img-fluid room-image" alt="객실 이미지">
			            <% } else { %>
			              <img src="<%= request.getContextPath() %>/images/no-image.png" class="img-fluid room-image" alt="기본 이미지">
			            <% } %>
			          </div>
			          <div class="col-md-6">
			            <p><strong>숙소명:</strong> <%= room.getRoomStayName() %></p>
			            <p><strong>타입:</strong> <%= room.getRoomType() %></p>
			            <p><strong>최대 인원:</strong> <%= room.getRoomPaxMax() %>명</p>
			            <p><strong>가격:</strong> ₩<%= room.getRoomPrice() %></p>
			            <hr/>
			            <p><strong>객실 상세 설명:</strong></p>
			            <div class="room-content"><%= room.getRoomContent().replaceAll("\n", "<br/>") %></div>
			          </div>
			        </div>
			      </div>
			    </div>
			  </div>
			</div> <!-- (modal)객실 상세정보 보기 ends -->

          </div>
        </div>
      </div>
      <% } %>
    </div>
  </div>

  <script>
/*     function selectRoom(roomNum, roomName, roomPrice) {
      alert("선택된 객실: " + roomName + " (가격: ₩" + roomPrice + ")");
      // 예약 폼 등으로 이동하거나, roomNum을 hidden 필드에 설정하는 식으로 처리
    } */
  </script>
</body>
</html>
