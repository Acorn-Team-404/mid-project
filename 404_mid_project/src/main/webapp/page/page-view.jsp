<%@page import="model.room.RoomDao"%>
<%@page import="java.util.List"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.page.PageDto"%>
<%@page import="model.page.PageDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.page.StayDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// get 방식 파라미터로 전달되는 글 번호, 숙소 번호 얻어내기
	long pageNum = Long.parseLong(request.getParameter("pageNum"));
	long stayNum = Long.parseLong(request.getParameter("stayNum"));
	
	// 참조값 얻어오기
	StayDao dao = StayDao.getInstance();
	
	// 페이지 정보
	PageDto pageDto = PageDao.getInstance().getByNum(pageNum, stayNum);
	if (pageDto == null) {
	    out.println("<script>alert('페이지 정보를 불러올 수 없습니다.'); history.back();</script>");
	    return;
	}
	
	// 슥소 정보
	StayDto stayDto = StayDao.getInstance().getBy(stayNum);
	if (stayDto == null) {
		out.println("<script>alert('숙소 정보를 불러올 수 없습니다.'); history.back();</script>");
		return;
	}
	
	// 객실 정보
	List<RoomDto> list = RoomDao.getInstance().getRoomListByRoomNum((int)stayNum);
	
	// 별점 평균, 별점 수
	double avgStar = dao.getAverageStar(stayNum);
	int reviewCount = dao.getReviewCount(stayNum);
	
	// 좌표
	//double stayLat = Double.parseDouble(stayDto.getStayLat());
	//double stayLong = Double.parseDouble(stayDto.getStayLong());
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
		<button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/booking/booking-page';">
    		예약하기
		</button>
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
		<p><%=pageDto.getPageContent() %></p>
	</div>
	
	<!-- 별점 리뷰 -->
	<div class="container mt-5" id="review">
		<h3>리뷰</h3>
		<p>후기 <%=reviewCount %>개, 평균 ★<%=String.format("%.2f", avgStar) %></p>
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
						<div class="accordion-body">
							<table>
								<thead>
									<tr>
										<th>객실</th>
										<th>인원(기준/최대)</th>
										<th>금액</th>
									</tr>
								</thead>
								<tbody>
									<%for (RoomDto room : list) { %>
                					<tr>
					                    <td><%=room.getRoomName() %></td>
					                    <td>(<%=room.getRoomAdultMax() %> / <%=room.getRoomPaxMax() %>)명</td>
					                    <td><%=String.format("%,d", room.getRoomPrice()) %></td>
                					</tr>
                					<%} %>
								</tbody>
							</table>
							<%=pageDto.getPageReserve() %>
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
							<%=pageDto.getPageGuide() %>
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
							<%=pageDto.getPageRefund() %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
	
	</script>
</body>
</html>