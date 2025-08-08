<%@page import="model.book.GuidelineDto"%>
<%@page import="model.room.RoomDao"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.image.ImageDao"%>
<%@page import="model.image.ImageDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String usersId = (String) request.getAttribute("usersId");
		Long stayNum = Long.parseLong(request.getParameter("stayNum"));
    /* if (usersId == null) {
        response.sendRedirect(request.getContextPath() + "/booking/submit");
        return;
    } */
    List<ImageDto> carouselImages = ImageDao.getInstance().getListByTargetLong("stay", stayNum);
    int size = carouselImages.size();
    List<RoomDto> minaRoomList = RoomDao.getInstance().getRoomListByStayNum(stayNum);
    
    GuidelineDto guide = (GuidelineDto) request.getAttribute("guide");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/booking/</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-mina/room-info.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-kcr/booking-page.css" />
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
<jsp:include page="/WEB-INF/include/alert-modal.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp" />
	<jsp:include page="/WEB-INF/include/alert-modal.jsp"/>
	<!-- 하단 영역 -start -->
	<div class="container my-4">
	<!-- 캐러셀 영역 -start -->
		<% if (size > 0) { %>
		<div class="">
			<div id="carouselExampleIndicators" class="carousel slide mb-4" data-bs-ride="carousel">
			  <div class="carousel-indicators">
			    <% for (int i = 0; i < size; i++) { %>
			      <button type="button"
			              data-bs-target="#carouselExampleIndicators"
			              data-bs-slide-to="<%= i %>"
			              class="<%= (i == 0) ? "active" : "" %>"
			              aria-current="<%= (i == 0) ? "true" : "" %>"
			              aria-label="Slide <%= i + 1 %>"></button>
			    <% } %>
			  </div>
			
			  <div class="carousel-inner ratio ratio-21x9">
			    <% for (int i = 0; i < size; i++) {
			         ImageDto img = carouselImages.get(i);
			    %>
			      <div class="carousel-item <%= (i == 0) ? "active" : "" %>">
			        <img src="<%= request.getContextPath() %>/show.img?imageName=<%= img.getImageSavedName() %>"
							     class="d-block w-100"
							     alt="<%= img.getImageOriginalName() %>">
			      </div>
			    <% } %>
			  </div>
			
			  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
			    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
			    <span class="visually-hidden">Previous</span>
			  </button>
			  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
			    <span class="carousel-control-next-icon" aria-hidden="true"></span>
			    <span class="visually-hidden">Next</span>
			  </button>
			</div>
			<% } else { %>
			  <p>등록된 이미지가 없습니다.</p>
			<% } %>
		</div>
		<!-- 캐러셀 영역 -end -->
		<form action="${pageContext.request.contextPath}/booking/submit" id="bookForm" method="post">
    	<!-- 이부분 필수 -->
    	<input type="hidden" name="bookStayNum" value="${param.stayNum}"/>
        <div class="row align-items-stretch">
            <!-- 왼쪽: 예약자 정보 -->
            <div class="col-md-7 d-flex flex-column justify-content-between">
            	<% for (RoomDto room : minaRoomList) {
								List<ImageDto> imageList = ImageDao.getInstance().getListByTargetLong("room", room.getRoomNum());
						    String carouselId = "carousel-room-" + room.getRoomNum();
						  %>
						  <div class="">
						    <div class="card mb-4 shadow-sm border border-light rounded bg-white shadow-sm mb-4">
						      <div class="row g-0">
						      		<!--  최상단 숙소명: -->
						  	
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
													객실 상세보기</button>
						          </div>
						        </div>
						        
<!-- 객실 상세정보 보기 영역 (모달처럼 펼치기) -->
<div class="modal fade" id="roomModal<%= room.getRoomNum() %>"
     tabindex="-1"
     aria-labelledby="roomModalLabel<%= room.getRoomNum() %>"
     aria-hidden="true">
  <!-- 모달 가로폭을 넓힌다 -->
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="roomModalLabel<%= room.getRoomNum() %>">
          <%= room.getRoomName() %> - 객실 상세정보
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
      </div>

      <!-- 모달 바디 높이를 제한한다 -->
      <div class="modal-body p-3">
        <div class="row g-3 align-items-stretch">
          <!-- 왼쪽: 캐러셀 영역 -->
          <div class="col-md-6">
            <%
              String modalCarouselId = "modal-carousel-room-" + room.getRoomNum();
              if (imageList != null && !imageList.isEmpty()) {
            %>
              <div id="<%= modalCarouselId %>" class="carousel slide h-100">
                <!-- 비율 유틸(ratio)을 제거하고 고정 높이를 준다 -->
                <div class="carousel-inner rounded overflow-hidden" style="height:40vh;"><%-- 캐러셀 높이를 고정한다. --%>
                  <% for (int j = 0; j < imageList.size(); j++) {
                       String savedName = imageList.get(j).getImageSavedName();
                  %>
                    <div class="carousel-item <%= (j == 0) ? "active" : "" %> h-100">
                      <img src="<%= request.getContextPath() %>/show.img?imageName=<%= savedName %>"
                           class="d-block w-100 h-100"
                           style="object-fit:cover; object-position:center;" <%-- 중앙 기준으로 위아래를 크롭한다. --%>
                           alt="객실 이미지 <%= j + 1 %>">
                    </div>
                  <% } %>
                </div>

                <% if (imageList.size() > 1) { %>
                  <button class="carousel-control-prev" type="button"
                          data-bs-target="#<%= modalCarouselId %>" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                  </button>
                  <button class="carousel-control-next" type="button"
                          data-bs-target="#<%= modalCarouselId %>" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                  </button>
                <% } %>
              </div>
            <% } else { %>
              <!-- 이미지가 없을 경우 기본 이미지 -->
              <div class="rounded overflow-hidden" style="height:40vh;">
                <img src="<%= request.getContextPath() %>/images/no-image.png"
                     class="w-100 h-100"
                     style="object-fit:cover; object-position:center;" <%-- 기본 이미지도 동일하게 크롭한다. --%>
                     alt="기본 이미지">
              </div>
            <% } %>
          </div>

          <!-- 오른쪽: 텍스트 영역 (스크롤) -->
          <div class="col-md-6">
            <div class="h-100" style="max-height:40vh; overflow:auto;"><%-- 텍스트가 길면 스크롤한다. --%>
              <p><strong>숙소명:</strong> <%= room.getRoomStayName() %></p>
              <p><strong>타입:</strong> <%= room.getRoomType() %></p>
              <p><strong>최대 인원:</strong> <%= room.getRoomPaxMax() %>명</p>
              <p><strong>가격:</strong> ₩<%= room.getRoomPrice() %></p>
              <hr/>
              <p><strong>객실 상세 설명:</strong><br/><%= room.getRoomContent().replaceAll("\n", "<br/>") %></p>
              <p><strong>편의 시설:</strong><br/><%= room.getRoomStayFacilities() %></p>
            </div>
          </div>
        </div>
      </div> <!-- /modal-body -->
    </div>
  </div>
</div> <!-- (modal)객실 상세정보 보기 ends -->
						
						      </div>
						    </div>
						  </div>
						  <% } %>
	            <div>
	                <div class="left-container mb-4">
	                    <h1 class="mb-4">예약자 정보</h1>
	                    <input type="hidden" name="usersNum" id="userNum" value="${usersNum}"/>
	
	                    <div class="d-flex mb-4">
	                        <label for="usersName" class="form-label col-3 mb-0">이름</label>
	                        <p class="mb-0">${usersName}</p>
	                        <input type="hidden" name="usersName" id="userName" value="${usersName}"/>
	                    </div>
	
	                    <div class="d-flex mb-4">
	                        <label for="email" class="form-label col-3 mb-0">이메일</label>
	                        <p class="mb-0">${email}</p>
	                        <input type="hidden" name="email" id="email" value="${email}"/>
	                    </div>
	
	                    <div class="d-flex mb-4">
	                        <label for="phone" class="form-label col-3 mb-0">전화번호</label>
	                        <p class="mb-0">${phone}</p>
	                        <input type="hidden" name="phone" id="phone" value="${phone}"/>
	                    </div>
	
	                    <div class="mb-4">
	                        <h2>옵션</h2>
	                        <p class="text-muted">숙박 시 제공되는 옵션을 선택하세요</p>                    
	                    		
							<div class="row mb-2">
								<div class="col-md-6 d-flex">
									<label class="form-label mb-0">추가 침대</label>
								</div>
								<div class="col-md-6 d-flex">
									<label class="form-label mb-0">도착</label>
								</div>
							</div>
	                        	<div class="row">
	                        		<div class="col-md-6 d-flex flex-column" style="gap: 0.5rem;">
	                        			<div>
	                        				<input class="form-check-input" type="checkbox" name="bed" value="extraBed" id="extraBed" data-label="간이 침대"/>
										<label class="form-check-label" for="extraBed">간이 침대</label>
	                        			</div>
									<div>
										<input class="form-check-input" type="checkbox" name="bed" value="infantBed" id="infantBed" data-label="유아 침대"/>
										<label class="form-check-label" for="infantBed">유아 침대</label>
									</div>
	                        		</div>
	                        		<div class="col-md-6 d-flex flex-column" style="gap: 0.5rem;">
	                        			<div>
	                        				<input class="form-check-input" type="radio" name="checkInTime" value="standard" id="checkInStandard" checked data-label="정규 시간"/>
										<label class="form-check-label" for="checkInStandard">정규 시간</label>
	                        			</div>
	                        			<div>
	                        				<input class="form-check-input" type="radio" name="checkInTime" value="early" id="checkInEarly" data-label="이른 체크인"/>
										<label class="form-check-label" for="checkInEarly">이른 체크인</label>
	                        			</div>
	                        			<div>
	                        				<input class="form-check-input" type="radio" name="checkInTime" value="late" id="checkInLate" data-label="늦은 도착"/>
										<label class="form-check-label" for="checkInLate">늦은 도착</label>
	                        			</div>
	                        		</div>
	                        	</div>    		
	                    </div>
	
	                    <div class="mb-3">
	                        <h2 class="mb-3">추가 요청사항</h2>
	                        <textarea name="bookRequest" id="bookRequest" class="form-control" rows="5" placeholder="요청사항을 입력해 주세요"></textarea>
	                    </div>
	                    
	                    <div class="mt-4 pt-3">
							<!-- 예약 안내사항 -->
							<div class="border-top py-3 mb-3">
							    <h5 class="fw-semibold">예약 안내사항</h5>
							    <p class="mb-0"><%= guide.getGuideInformation().replaceAll("\n", "<br/>") %></p>
							</div>
							
							<!-- 이용 안내 -->
							<div class="border-top py-3 mb-3">
							    <h5 class="fw-semibold">이용 안내</h5>
							    <p class="mb-0"><%= guide.getStayPolicy().replaceAll("\n", "<br/>") %></p>
							</div>
							
							<!-- 환불 규정 -->
							<div class="border-top py-3">
							    <h5 class="fw-semibold">환불 규정</h5>
							    <p class="mb-0"><%= guide.getRefundPolicy().replaceAll("\n", "<br/>") %></p>
							</div>
						</div>
					</div>
				</div>	            
            </div>
            <!-- 오른쪽: 예약 요약 -->
            <div class="col-12 col-md-5">
                <div class="right-container-fixed sticky" style="top:100px;">
                    <input type="hidden" name="bookStayNum" id="bookStayNum" value="${stay.stayNum}" />

                    <select name="bookRoomNum" id="bookRoomNum" class="form-select">
                        <option value="" disabled selected>객실을 선택하세요</option>
                        <% 
                            List<RoomDto> roomList = (List<RoomDto>) request.getAttribute("roomList");
                            if (roomList != null && !roomList.isEmpty()) {
                                for (RoomDto room : roomList) {
                        %>
                            <option value="<%= room.getRoomNum() %>" data-price="<%= room.getRoomPrice() %>" data-name="<%= room.getRoomName() %>">
                                <%= room.getRoomName() %>
                            </option>
                        <% 
                                }
                            } else { 
                        %>
                            <option disabled>객실 정보가 없습니다</option>
                        <% } %>
                    </select>
                    
                    <!-- 체크인 체크아웃 선택 한 번에 해보기 -->
                    <div class="mb-3 mt-3">
						<label for="dateRange" class="form-label">날짜 선택</label>
						<input type="text" id="dateRange" class="form-control" placeholder="체크인 - 체크아웃"/>
                    		<input type="hidden" name="checkIn" id="checkIn" />
						<input type="hidden" name="checkOut" id="checkOut" />
						
						<!-- 선택한 날짜 표시 -->
						<div class="p-3 mt-2 rounded border shadow-sm" id="dateSummary" 
							style="display: none; background-color: #e9f5ff; color: #0c5460; font-size: 0.95rem;">
						</div>
                    		
                    </div>

                    <div class="mb-3">
                        <label class="form-label">투숙 인원 선택</label>
                        
                        <!-- 성인 -->
                        <div class="count-box">
                            <span class="me-auto">성인<br/><small class="text-muted">만 12세 이상</small></span>
                            <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('adult', -1)">-</button>
                            <span id="adultCount">2</span>
                            <input type="hidden" name="adult" id="adult" value="2" min="0"/>
                            <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('adult', 1)">+</button>
                        </div>

                        <!-- 어린이 -->
                        <div class="count-box">
                            <span class="me-auto">어린이<br/><small class="text-muted">만 2~11세</small></span>
                            <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('children', -1)">-</button>
                            <span id="childrenCount">0</span>
                            <input type="hidden" name="children" id="children" value="0"/>
                            <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('children', 1)">+</button>
                        </div>

                        <!-- 유아 -->
                        <div class="count-box">
                            <span class="me-auto">유아<br/><small class="text-muted">만 2세 미만</small></span>
                            <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('infant', -1)">-</button>
                            <span id="infantCount">0</span>
                            <input type="hidden" name="infant" id="infant" value="0"/>
                            <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('infant', 1)">+</button>
                        </div>
                    </div>

					<!-- 예약 요악 -->
                    <div class="mb-4 border-top">
					    <div class="d-flex mb-4 mt-3">
					        <label class="form-label col-3 mb-0 fs-6 fw-normal text-dark">객실 이름</label>
					        <p class="mb-0 fs-6 fw-normal text-dark" id="selectedRoomNameDisplay">-</p>
					    </div>
					
					    <div class="d-flex mb-4">
					        <label class="form-label col-3 mb-0 fs-6 fw-normal text-dark">객실 가격</label>
					        <p class="mb-0 fs-6 fw-normal text-dark">₩ <span id="roomPrice">선택하세요</span></p>
					    </div>
					
					    <div class="d-flex mb-4">
					        <label class="form-label col-3 mb-0 fs-6 fw-normal text-dark">추가 침대</label>
					        <p class="mb-0 fs-6 fw-normal text-dark" id="bedOption">선택된 옵션이 없습니다</p>
					    </div>
					
					    <div class="d-flex mb-4">
					        <label class="form-label col-3 mb-0 fs-6 fw-normal text-dark">도착 시간</label>
					        <p class="mb-0 fs-6 fw-normal text-dark" id="checkInOption">정규 시간</p>
					    </div>
					
					    <div class="d-flex mb-4">
					        <label class="form-label col-3 mb-0 fs-6 fw-normal text-dark">총 인원</label>
					        <p class="mb-0 fs-6 fw-normal text-dark"><span id="totPerson">1</span>명</p>
					    </div>
					
					    <div class="d-flex mb-2 pt-1">
					        <label class="form-label col-3 mb-0 fs-6 fw-normal text-dark">총액</label>
					        <p class="mb-0 fs-6 fw-normal text-dark">₩ <span id="totalAmount">0</span></p>
					    </div>
					</div>


                    <!-- JS에서 설정한 값을 서블릿에 넘기기 위한 hidden input들 -->
                    <input type="hidden" name="selectedBed" id="selectedBed" value=""/>
                    <input type="hidden" name="selectedCheckInTime" id="selectedCheckInTime" value="standard"/>
                    <input type="hidden" name="totalAmountValue" id="totalAmountValue" value="0"/>

                    <button type="submit" class="btn btn-primary w-100">예약하기</button>
                </div>
            </div>
       
        </div>
     </form>
	</div> <!-- 채린 영력 -end -->
	<!-- 하단 영역 -end -->
	<script>
	/* 미나 영역 -start*/
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
	<script src="${pageContext.request.contextPath}/js/booking/book.js"></script>
		<jsp:include page="/WEB-INF/include/footer.jsp" />
</body>
</html>