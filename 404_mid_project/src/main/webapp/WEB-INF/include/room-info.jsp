<%@page import="model.image.ImageDto"%>
<%@page import="model.room.RoomDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.room.RoomDto" %>
<%@ page import="model.image.ImageDao" %>

<%
  // 현재 숙소 번호를 파라미터로 받는다고 가정
  int stayNum = Integer.parseInt(request.getParameter("stayNum"));
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
		    	
		    	<!-- ✅ 첫 번째 이미지만 보여주는 영역 -->
				<div class="col-md-5">
				  	<% if (imageList != null && !imageList.isEmpty()) { %>
				    <div class="rounded overflow-hidden w-100 h-100">
				      <img src="<%= request.getContextPath() %>/show.img?imageName=<%= imageList.get(0).getImageSavedName() %>"
				           class="w-100 h-100 object-fit-cover"
				           alt="객실 이미지">
				    </div>
				  <% } else { %>
				    <!-- 이미지가 없을 때도 동일한 비율 유지 -->
				    <div class="rounded overflow-hidden w-100 h-100">
				      <img src="<%= request.getContextPath() %>/images/no-image.png"
				           class="w-100 h-100 object-fit-cover"
				           alt="기본 이미지">
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
                <button type="button" class="btn btn-dark btn-room-select"
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
			        
			        
					<!-- 모달창 안에 이미지 슬라이드 영역 -->
					<div class="col-md-6">
					  <% 
					    String modalCarouselId = "modal-carousel-room-" + room.getRoomNum();
					    if (imageList != null && !imageList.isEmpty()) { 
					  %>
					    <!-- 캐러셀 컨테이너 -->
					    <div id="<%= modalCarouselId %>" class="carousel slide" data-bs-ride="carousel">
					      <div class="carousel-inner rounded" style="height: 240px;">
					        <% for (int j = 0; j < imageList.size(); j++) {
					              String savedName = imageList.get(j).getImageSavedName();
					        %>
					          <div class="carousel-item <%= (j == 0) ? "active" : "" %>">
					            <img src="<%= request.getContextPath() %>/show.img?imageName=<%= savedName %>"
					                 class="d-block w-100 h-100 object-fit-cover"
					                 alt="객실 이미지 <%= j + 1 %>">
					          </div>
					        <% } %>
					      </div>
					      <% if (imageList.size() > 1) { %>
					        <button class="carousel-control-prev" type="button" data-bs-target="#<%= modalCarouselId %>" data-bs-slide="prev">
					          <span class="carousel-control-prev-icon"></span>
					        </button>
					        <button class="carousel-control-next" type="button" data-bs-target="#<%= modalCarouselId %>" data-bs-slide="next">
					          <span class="carousel-control-next-icon"></span>
					        </button>
					      <% } %>
					    </div>
					  <% } else { %>
					    <!-- 이미지가 없을 경우 기본 이미지 -->
					    <div class="ratio ratio-4x3 rounded overflow-hidden">
					      <img src="<%= request.getContextPath() %>/images/no-image.png"
					           class="img-fluid h-100 w-100 object-fit-cover"
					           alt="기본 이미지">
					    </div>
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
    } 
*/
		//모달이 닫힐 때 내부 스크린리더에서 내용 읽히는 것을 hidden 처리하기 위해
		const modals = document.querySelectorAll('.modal');
		modals.forEach(modal => {
			// 닫히기 직전에 포커스를 다른 곳으로 옮긴다.
			modal.addEventListener('hide.bs.modal', () => {
				//  모달 내부에 포커스가 있으면, body 또는 지정한 요소로 포커스를 이동
				if (modal.contains(document.activeElement)) {
					document.body.focus(); // 또는 원하는 버튼, div 등
				}
	        });
			// 완전히 닫힌 후 처리 (보조)
			modal.addEventListener('hidden.bs.modal', () => {
				// 혹시라도 포커스가 남아있다면 한 번 더 옮기기
			    if (modal.contains(document.activeElement)) {
			       document.body.focus();
			       }
				});
			});
		
  </script>
</body>
</html>
