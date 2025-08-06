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
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/booking/</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-mina/room-info.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-kcr/booking-page.css" />
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />
	<!-- 캐러셀 영역 -start -->
	<% if (size > 0) { %>
	<div class="w-75 mx-auto">
		<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
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
	
	<!-- 하단 영역 -start -->
	<div class="container my-5">
		<form action="${pageContext.request.contextPath}/booking/submit" method="post">
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
						    <div class="card mb-4 shadow-sm">
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
	            <div class="">
	                <div class="left-container">
	                    <h1>예약자 정보</h1>
	                    <input type="hidden" name="usersNum" id="userNum" value="${usersNum}"/>
	
	                    <div class="mb-3">
	                        <label for="usersName" class="form-label">이름</label>
	                        <p>${usersName}</p>
	                        <input type="hidden" name="usersName" id="userName" value="${usersName}"/>
	                    </div>
	
	                    <div class="mb-3">
	                        <label for="email" class="form-label">이메일</label>
	                        <p>${email}</p>
	                        <input type="hidden" name="email" id="email" value="${email}"/>
	                    </div>
	
	                    <div class="mb-3">
	                        <label for="phone" class="form-label">전화번호</label>
	                        <p>${phone}</p>
	                        <input type="hidden" name="phone" id="phone" value="${phone}"/>
	                    </div>
	
	                    <div class="mb-4">
	                        <h2>옵션</h2>
	                        <p>숙박 시 제공되는 옵션을 선택하세요</p>
	
	                        <label class="form-label">추가 침대</label><br/>
	                        <div class="form-check form-check-inline">
	                            <input class="form-check-input" type="checkbox" name="bed" value="extraBed" id="extraBed" data-label="간이 침대"/>
	                            <label class="form-check-label" for="extraBed">간이 침대</label>
	                        </div>
	                        <div class="form-check form-check-inline">
	                            <input class="form-check-input" type="checkbox" name="bed" value="infantBed" id="infantBed" data-label="유아 침대"/>
	                            <label class="form-check-label" for="infantBed">유아 침대</label>
	                        </div>
	
	                        <div class="mt-3">
	                            <label class="form-label">도착</label><br/>
	                            <div class="form-check">
	                                <input class="form-check-input" type="radio" name="checkInTime" value="standard" id="checkInStandard" checked data-label="정규 시간"/>
	                                <label class="form-check-label" for="checkInStandard">정규 시간</label>
	                            </div>
	                            <div class="form-check">
	                                <input class="form-check-input" type="radio" name="checkInTime" value="early" id="checkInEarly" data-label="이른 체크인"/>
	                                <label class="form-check-label" for="checkInEarly">이른 체크인</label>
	                            </div>
	                            <div class="form-check">
	                                <input class="form-check-input" type="radio" name="checkInTime" value="late" id="checkInLate" data-label="늦은 도착"/>
	                                <label class="form-check-label" for="checkInLate">늦은 도착</label>
	                            </div>
	                        </div>
	                    </div>
	
	                    <div class="mb-4">
	                        <h2>추가 요청사항</h2>
	                        <textarea name="bookRequest" id="bookRequest" class="form-control" rows="5" placeholder="요청사항을 입력해 주세요."></textarea>
	                    </div>
	
	                    <h2>예약 안내사항</h2>
						<p>
						    정확한 객실 요금은 일정 선택 후 확인할 수 있습니다.<br/>
						    객실 타입에 따라 최대 투숙 인원이 상이합니다.<br/>
						    스탠다드 객실 : 기준 인원 2인, 최대 인원 2인까지 이용할 수 있습니다.<br/>
						    트윈 객실 : 기준 인원 2인, 최대 인원 3인까지 이용할 수 있습니다.<br/>
						    패밀리 객실 : 기준 인원 2인, 최대 인원 4인까지 이용할 수 있습니다.<br/>
						    기준 인원 초과 시, 1인 1박당 추가 요금이 발생합니다.<br/><br/>
						    
						    예약 가능일<br/>
						    당일 예약 및 입실이 가능합니다.<br/><br/>
						    
						    반려 동물<br/>
						    반려 동물은 동반이 불가한 숙소입니다.
						</p>
	
	                    <h2>이용 안내</h2>
	                    <p>
	                    	체크인은 오후 3시 이후부터, 체크아웃은 오전 11시까지 입니다.<br/>
							예약 시, 최대 인원을 초과하는 인원은 입실이 불가합니다.<br/>
							예약 인원 외 방문객의 출입은 제한됩니다.<br/>
							숙소 내의 모든 공간에서 절대 금연입니다. 위반 시 특수청소비가 청구됩니다.<br/>
							침구나 비품에 심각한 오염이 발생한 경우, 파손 및 분실 등에 변상비가 청구됩니다.<br/>
							늦은 시간 외부로 소음이 발생하지 않도록 배려 부탁드립니다.<br/>
							협의되지 않은 상업 사진 및 영상 촬영(광고용, 제품사진, 쇼핑몰, SNS마켓 포함), 드론 촬영은 불가합니다.<br/>
	                    </p>
	                    <h2>환불 규정</h2>
	                    <p class=>
	                    	체크인 10일 전까지 : 총 결제금액의 100% 환불<br/>
							체크인 9일 전까지 : 총 결제금액의 90% 환불<br/>
							체크인 8일 전까지 : 총 결제금액의 80% 환불<br/>
							체크인 7일 전까지 : 총 결제금액의 70% 환불<br/>
							체크인 6일 전까지 : 총 결제금액의 60% 환불<br/>
							체크인 5일 전까지 : 총 결제금액의 50% 환불<br/>
							체크인 4일 전까지 : 총 결제금액의 40% 환불<br/>
							체크인 3일 전부터 : 변경 및 환불 불가
	                    </p>
	               
	                    <!-- <h2>판매자 정보</h2> -->
	                </div>
	            </div>
	            
            </div>
            <!-- 오른쪽: 예약 요약 -->
            <div class="col-5">
                <div class="right-container-fixed">
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

                    <div class="mb-3 mt-3">
                        <label for="checkIn" class="form-label">체크인 날짜</label>
                        <input type="date" name="checkIn" id="checkIn" class="form-control"/>

                        <label for="checkOut" class="form-label mt-2">체크아웃 날짜</label>
                        <input type="date" name="checkOut" id="checkOut" class="form-control"/>
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

                        <strong class="mt-2">총 인원 : <span id="totPerson">1</span>명</strong>
                    </div>

                    <div class="mb-4">
                        <strong>객실 이름 :</strong> <span id="selectedRoomNameDisplay"></span><br/>
                        <strong>객실 가격 :</strong> ₩ <span id="roomPrice">선택하세요</span><br />
                        <strong>추가 침대 :</strong> <span id="bedOption"></span><br/>
                        <strong>도착 시간 :</strong> <span id="checkInOption"></span><br/>
                        <strong>총액 :</strong> ₩ <span id="totalAmount"></span>
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
	/* 미나 영역 -end */


	</script>
	<script src="${pageContext.request.contextPath}/js/booking/book.js"></script>
</body>
</html>