<%@page import="model.page.PageDto"%>
<%@page import="model.page.PageDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.page.StayDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// get 방식 파라미터로 전달되는 숙소 번호 얻어내기
	long stayNum = Long.parseLong(request.getParameter("stay_num"));
	
	// 참조값 얻어오기
	StayDao dao = StayDao.getInstance();
	
	// 슥소 정보
	StayDto stayDto = dao.getByNum(stayNum);
	
	// 페이지 정보
	PageDto pageDto = dao.getPage(stayNum);
	
	// 별점 평균, 별점 수
	double avgStar = dao.getAverageStar(stayNum);
	int reviewCount = dao.getReviewCount(stayNum);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/view</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>


	<!-- Carousel 영역 -->
	
	
	<!-- 숙소 정보 -->
	<div class="container col-8">
		<h2 class="stay-name"><%=stayDto.getStayName() %></h2>
		<p class="stay-location"><%=stayDto.getStayLoc() %></p>
		<p class="stay-review">
			<span class="reviews">★ <%=String.format("%.1f", avgStar) %>점 / 후기 <%=reviewCount %>개</span>
		</p>
	</div>
	<div class="col-4">
		<button type="button" class="btn btn-primary" onclick="#">예약하기</button>
	</div>
	
	<!-- 네비게이션 바 -->
	<nav class="navbar navbar-expand-lg bg-body-tertiary sticky-top">
		<div class="container-fluid">
			<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item">
						<a class="nav-link" href="#info">스테이</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#review">리뷰</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#loc">위치</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#notice">안내</a>
					</li>
				</ul>
			</div>
		</div>
	</nav>
	
	<!-- 숙소 소개 -->
	<div class="container mt-5" id="info">
		<h3>스테이 소개</h3>
		<p><%= pageDto.getPageContent() %></p>
	</div>
	
	<!-- 별점 리뷰 -->
	<div class="container mt-5" id="review">
		<h3>리뷰</h3>
		<p>후기 <%=reviewCount%>개, 평균 ★<%=String.format("%.2f", avgStar)%></p>
	</div>
	
	<!-- 카카오 맵 -->
	<div class="container mt-5" id="loc">
		<h3>위치</h3>
		<div id="map" style="width:500px;height:400px;">
			<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9b6864c72d30ce2e92d016819ab66440"></script>
			<script>
				var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
				mapOption = { 
					center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
					level: 3 // 지도의 확대 레벨
				};
	
				var map = new kakao.maps.Map(mapContainer, mapOption);
			
				// 마커가 표시될 위치입니다 
				var markerPosition  = new kakao.maps.LatLng(33.450701, 126.570667); 
		
				// 마커를 생성합니다
				var marker = new kakao.maps.Marker({
					position: markerPosition
				});
			
				// 마커가 지도 위에 표시되도록 설정합니다
				marker.setMap(map);
			
				var iwContent = '<div style="padding:5px;">Hello World!
									<br>
										<a href="https://map.kakao.com/link/map/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">큰지도보기</a>
										<a href="https://map.kakao.com/link/to/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">길찾기</a>
								</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
				iwPosition = new kakao.maps.LatLng(33.450701, 126.570667); //인포윈도우 표시 위치입니다
			
				// 인포윈도우를 생성합니다
				var infowindow = new kakao.maps.InfoWindow({
					position : iwPosition, 
					content : iwContent 
				});
					  
				// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
				infowindow.open(map, marker);
			</script>
		</div>
	</div>
	
	<!-- 숙소 방침 -->
	<div class="container mt-5">
		<div class="Container" id="notice">
			<h1>안내사항</h1>
			<div class="accordion" id="accordionExample">
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#navOne" aria-expanded="true" aria-controls="collapseOne">
						예약 안내
						</button>
					</h2>
					<div id="navOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
						<div id="class="accordion-body">
							<table>
								<thead>
									<tr>
										<th>객실</th>
										<th>인원(기준/최대)</th>
										<th>금액</th>
									</tr>
								</thead>
							</table>
							<%= pageDto.getPageReserve() %>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#navTwo" aria-expanded="false" aria-controls="collapseTwo">
						이용 안내
						</button>
					</h2>
					<div id="navTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
						<div class="accordion-body">
							<%= pageDto.getPageGuide() %>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#navThree" aria-expanded="false" aria-controls="collapseThree">
						환불 규정
						</button>
					</h2>
					<div id="navThree" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
						<div class="accordion-body">
							<%= pageDto.getPageRefund() %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>