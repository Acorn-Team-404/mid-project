<%@page import="controller.tester.RoomTestDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.room.RoomDto"%>
<%@page import="java.util.List"%>
<%@page import="model.room.RoomDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
    int stayNum = 100;
    List<RoomDto>roomList = RoomDao.getInstance().getRoomListByStayNum(stayNum);
    
    StayDto stay = RoomTestDao.getInstance().getByNum(stayNum);
%>

<div class="container my-5">
	<!-- for 문 시작 -->
	<% for (RoomDto room : roomList){ %> 
    <div class="mb-2">
        <label class="form-label" hidden>숙소명!</label>
        <p class="room-type"><%=stay.getStayName() %>숙소명</p>
        <input type="hidden" name="roomType" id="roomType" value="<%=room.getRoomType() %>">
    </div>
     <form action="${pageContext.request.contextPath}/booking/roomtypes" method="post" id="roomInfoForm">	
	    <div class="row">
	    	<!-- 객실 타입 카드 영역 -->
	        <div class="col-md-7" id="roomList">
	           <!-- for 문 시작 -->
			   
	            <!-- 예시 카드 1개 구조. (반복 구조는 Servlet에서 roomTypes로 전달됨) -->
			            <!-- data-room-num= 속성 추가해야 info.js가 정상 작동함  -->
		 	            <div class="room-card" data-room-num="<%= room.getRoomNum()%>" style="cursor: pointer;">
			            	<input type="hidden" name="roomCard" id="roomCard" value=" " />
			                <div class="row g-0">
			                
			               		<!-- 왼쪽 숙소 이미지 영역 -->
			                    <div class="col-md-5">
									<img src="${pageContext.request.contextPath}/images/indexc01.jpg" class="img-fluid room-image" alt="임의 객실 이미지">
			                    </div>
			                    
			                    <!-- 오른쪽: 객실 정보 텍스트 및 객실 선택 버튼 영역 -->
			                    <div class="col-md-7 p-4 d-flex flex-column justify-content-between">
			                        <div>
			                            <div class="mb-2">
			                                <label class="form-label" hidden>객실 타입</label>
			                                <p class="room-type"><%= room.getRoomType() %> </p>   <!-- 스탠다드 객실 -->
			                                <input type="hidden" name="roomType" id="roomType" value="<%= room.getRoomType() %>">
			                            </div>
				                            
			                            <div class="mb-2">
			                                <label class="form-label">최대 인원</label>
			                                <p class="room-pax-max"><%=room.getRoomPaxMax() %>명</p>
			                                <input type="hidden" name="roomPaxMax" id="roomPaxMax" value="<%= room.getRoomPaxMax() %>">
			                            </div>
			
			                            <div class="mb-2">
			                                <label class="form-label">가격</label>
			                                <p class="room-price">₩<%=room.getRoomPrice() %></p>
			                                <input type="hidden" name="roomPrice" id="roomPrice" value="<%=room.getRoomPrice() %>">
			                            </div>
			                        </div>
			                        <div class="mt-3 d-flex justify-content-between align-items-center">
			                            <button type="button" class="btn btn-room-select" data-room-num="<%=room.getRoomNum() %>" 
			                            data-room-name="<%=room.getRoomType() %>" data-room-price="<%=room.getRoomPrice() %>">
			                                객실 선택
			                                
			                            </button>
			                        </div>
			                    </div>
			                    
			                </div>
			               
				            <!-- 상세정보 영역 (모달처럼 펼치기)  style="display: none;"-->
				            <div class="room-detail mt-3"  style="display: none;">
				                <hr/>
				                <div class="col-md-5">
				                	<!--  이미지 get 해야됨  -->
									<img src="${pageContext.request.contextPath}/images/indexc01.jpg" class="img-fluid room-image" alt="임의 객실 이미지">
			                  </div>
				                <hr/>                    
			                    <div class="mb-2">
			                        <label class="form-label">객실 상세 정보 및 어메니티</label>
			                        <pre class="room-content">
			                        ${roomContent}
			                        객실 설명: 채광이 좋은 42m²의 고층 객실로, 창밖으로 펼쳐지는 탄천과 도심의 풍경이 여유를 더합니다.
			                        객실 어메니티: 55인치 LED TV, 개별 난방 조절기, 아기 침대 (요청 시 제공), 네스프레소 커피 머신,
			                        다리미와 다리미 판, 객실 내 금고, TV 시청이 가능한 욕조와 레인 샤워, 목욕가운과 슬리퍼, 미니바와 생수,
			                        목욕가운과 슬리퍼, 파크 클럽 (피트니스, 수영장) 이용 가능
			                        </pre>
			                        <input type="hidden" name="roomContent" id="roomContent" value="<%=room.getRoomContent() %>">
			                    </div>	                
				            </div>
			            </div>
			            <%} %> <!-- for 문 ends  -->
	            <!-- 위 구조 반복되서 각 room 정보에 따라 렌더링. 객실타입 보여질 예정 -->     
	            
	        </div>
	    </div>
    </form>
</div>
