<%@page import="model.admin.StayInfoDao"%>
<%@page import="model.admin.StayInfoDto"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.room.RoomDao"%>
<%@page import="model.image.ImageDao"%>
<%@page import="model.image.ImageDto"%>
<%@page import="java.util.List"%>
<%@page import="model.review.ReviewDao"%>
<%@page import="model.review.ReviewDto"%>
<%@page import="model.book.GuidelineDao"%>
<%@page import="model.book.GuidelineDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	long stayNum=Long.parseLong(request.getParameter("stayNum"));
	StayInfoDto stay = StayInfoDao.getInstance().getByNum(stayNum);
	
	long guideId = 1; //숙소별 가이드라인으로 변경되면 수정
	GuidelineDto guide = GuidelineDao.getInstance().getByGuideId(guideId);
	
	long pageNum=1; //리뷰 페이지 번호
	String strPageNum=request.getParameter("pageNum");
	if(strPageNum != null){
		pageNum=Long.parseLong(strPageNum);
	}
	
	//한 페이지에 몇개씩 표시할 것인지
	final long PAGE_ROW_COUNT=3;
		
	//하단 페이지를 몇개씩 표시할 것인지
	final long PAGE_DISPLAY_COUNT=7;
	
	//보여줄 페이지의 시작,끝 ROWNUM
	long startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT;
	long endRowNum=pageNum*PAGE_ROW_COUNT;
	
	//하단 시작,끝 페이지 번호
	long startPageNum = 1 + ((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
	long endPageNum=startPageNum+PAGE_DISPLAY_COUNT-1;
	
	long totalRow=0; //전체 리뷰 개수
	totalRow=ReviewDao.getInstance().getCountAll(stayNum);
	
	long totalPageCount=(long)Math.ceil((double)totalRow/PAGE_ROW_COUNT);
	if(endPageNum > totalPageCount){
		endPageNum=totalPageCount;
	}
	
	ReviewDto dto=new ReviewDto();
	dto.setStartRowNum(startRowNum);
	dto.setEndRowNum(endRowNum);
	dto.setReviewStayNum(stayNum);
	List<ReviewDto> list=ReviewDao.getInstance().selectPageByNum(dto);
    List<ImageDto> carouselImages = ImageDao.getInstance().getListByTargetLong("stay", stayNum);
    int size = carouselImages.size();
    List<RoomDto> minaRoomList = RoomDao.getInstance().getRoomListByStayNum(stayNum);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/view.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"/>
<style>
	.section-line{
		border-top: 3px solid #dee2e6;
		border-bottom: 3px solid #dee2e6;
		margin-bottom: 1rem;
		padding: 0.5rem;
		padding-top: 0;
	}
	.section{
		scroll-margin-top: 120px;
	}
	.section-title{
		border-bottom: 1px solid #dee2e6;
		margin-top: 0;
		padding: 0.5rem;
	}
	.star { color: gold; font-size: 1.2rem; }
    .star.gray { color: lightgray; font-size: 1.2rem; }
    .review-box {
    	border: 1px solid #ccc;
    	padding: 15px;
    	margin-bottom: 15px;
    	border-radius: 8px;
	}
	/* 컨텐츠 출력 이미지 제어 */
	.fs-7 img {
  max-width: 100%;    /* 부모 영역을 넘어가지 않게 */
  height: auto;       /* 비율 유지 */
  display: block;     /* inline gap 방지 */
  margin: 0 auto;     /* 중앙 정렬 */
	}
</style>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />
		<!-- 캐러셀 영역 -start -->
		<div class="container my-4">
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
							     style="height:100%; object-fit:cover; object-position:center;"
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
	<div class="container d-flex justify-content-between align-items-center bg-light">
		<div>
			<p class="fs-5 mb-0"><%=stay.getStayName() %></p>
			<p><%=stay.getStayLoc().replaceAll("\n", "<br>") %></p>
		</div>
		<div>
			<a class="btn btn-sm btn-primary me-2" href="${pageContext.request.contextPath }/booking/submit?stayNum=<%=stayNum %>">예약하기</a>
			<a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath }/inquiry/new-inquiry.jsp?stayNum=<%=stayNum %>">문의하기</a>
		</div>
	</div>

	<div class="container">
	    <!-- md 이하에서 상단 고정 가로 네비게이션 -->
	    <div class="d-lg-none sticky-top" style="top:78px; z-index: 1020;">
	        <div class="bg-white border-bottom p-2 mb-3">
	            <ul class="nav nav-pills justify-content-center">
	                <li class="nav-item me-3">
	                    <a class="nav-link py-1 px-2 small" href="#content-section">소개</a>
	                </li>
	                <li class="nav-item me-3">
	                    <a class="nav-link py-1 px-2 small" href="#review-section">리뷰</a>
	                </li>
	                <li class="nav-item me-3">
	                    <a class="nav-link py-1 px-2 small" href="#location-section">위치</a>
	                </li>
	                <li class="nav-item">
	                    <a class="nav-link py-1 px-2 small" href="#guide-section">안내</a>
	                </li>
	            </ul>
	        </div>
	    </div>
	
	    <!-- 데스크탑: 2열 레이아웃, 태블릿/모바일: 1열 레이아웃 -->
	    <div class="row">
	        <!-- lg 이상에서만 왼쪽 네비게이션 표시 -->
	        <div class="col-lg-3 d-none d-lg-block">
	            <div class="sticky-top" style="top:100px;">
	                <div class="p-3 rounded">
	                    <ul class="nav nav-pills flex-column">
	                        <li class="nav-item mb-1 border-bottom">
	                            <a class="nav-link py-2" href="#content-section">소개</a>
	                        </li>
	                        <li class="nav-item mb-1 border-bottom">
	                            <a class="nav-link py-2" href="#review-section">리뷰</a>
	                        </li>
	                        <li class="nav-item mb-1 border-bottom">
	                            <a class="nav-link py-2" href="#location-section">위치정보</a>
	                        </li>
	                        <li class="nav-item mb-1 border-bottom">
	                            <a class="nav-link py-2" href="#guide-section">안내사항</a>
	                        </li>
	                    </ul>
	                </div>
	            </div>
	        </div>
			<div class="col-lg-9 col-12">
				<div class="section section-line mt-2" id="content-section"> <!-- 소개글 -->
					<p class="section-title">소개글</p>
					<div class="fs-7"><%=stay.getStayContent().replaceAll("\n", "<br>") %></div>
				</div>
				<div class="section section-line" id="review-section"> <!-- 리뷰 -->
					<div class="d-flex section-title justify-content-between align-items-center">
						<div>리뷰</div>
						<div class="text-end">전체 리뷰 개수 : <%=totalRow %></div>
					</div>
					<div class="mt-3">
						<!-- 여기 -->
						<%if (totalRow == 0) {%>
						    <p class="fs-7">등록된 리뷰가 없습니다.</p>
						<%} else {%>
							<%for(ReviewDto tmp:list) {%>
								<div class="review-box">
									<strong><%= tmp.getUsersId() %></strong> | <%= tmp.getCreatedAt() %><br/>
									<div>
										<%for (int i = 1; i <= 5; i++) {%>
											<%if (i <= tmp.getRating()) {%>
												<span class="star">★</span>
											<%} else {%>
												<span class="star gray">★</span>
											<%} %>
										<%} %>
									</div>
									<p><%= tmp.getComment() %></p>
								</div>
							<%} %>
						<%} %>
					</div>
					<div>
						<ul class="pagination">
							<%-- startPageNum 이 1이 아닐때 이전 page 가 존재하기 때문에... --%>
							<%if(startPageNum != 1){ %>
								<li class="page-item">
									<a class="page-link" href="view.jsp?pageNum=<%=pageNum%>&rPageNum=<%=startPageNum-1 %>">&lsaquo;</a>
								</li>
							<%} %>			
							<%for(long i=startPageNum; i<=endPageNum ; i++){ %>
								<li class="page-item">
									<a class="page-link <%= i==pageNum ? "active":"" %>" href="view.jsp?pageNum=<%=pageNum%>&rPageNum=<%=i %>"><%=i %></a>
								</li>
							<%} %>
							<%-- endPageNum 이 totalPageCount 보다 작을때 다음 page 가 있다 --%>		
							<%if(endPageNum < totalPageCount){ %>
								<li class="page-item">
									<a class="page-link" href="view.jsp?pageNum=<%=pageNum%>&rPageNum=<%=endPageNum+1 %>">&rsaquo;</a>
								</li>
							<%} %>	
						</ul>
					</div>
				</div>
				<div class="section section-line" id="location-section"> <!-- 위치정보 -->
					<p class="section-title">위치정보</p>
					<div class="mb-3">
				        <p class="fs-7"><strong>주소:</strong> <%=stay.getStayAddr()%></p>
				    </div>
				    <!-- 지도 include -->
				    <%
					    // include할 JSP에 데이터 전달
					    request.setAttribute("stayAddress", stay.getStayAddr());
					    request.setAttribute("stayName", stay.getStayName());
					%>
				    <jsp:include page="/WEB-INF/include/map.jsp"/>
				</div>
				<div class="section" id="guide-section"> <!-- 안내사항 -->
					<div class="section-line">
						<p class="section-title">예약안내</p>
						<div class="fs-7"><%=guide.getGuideInformation().replaceAll("\n", "<br>") %></div>
					</div>
					<div class="section-line">
						<p class="section-title">이용안내</p>
						<div class="fs-7"><%=guide.getStayPolicy().replaceAll("\n", "<br>") %></div>
					</div>
					<div class="section-line">
						<p class="section-title">환불규정</p>
						<div class="fs-7"><%=guide.getRefundPolicy().replaceAll("\n", "<br>") %></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	<jsp:include page="/WEB-INF/include/footer.jsp" />
	<script>
		document.addEventListener('DOMContentLoaded', function() {
		    const navbarCollapse = document.querySelector('.navbar-collapse');
		    const stickyNav = document.querySelector('.d-lg-none.sticky-top');
		    
		    if (navbarCollapse && stickyNav) {
		        // 네비바가 열릴 때
		        navbarCollapse.addEventListener('show.bs.collapse', function() {
		            stickyNav.style.display = 'none'; // 숨기기
		        });
		        
		        // 네비바가 닫힐 때
		        navbarCollapse.addEventListener('hide.bs.collapse', function() {
		            stickyNav.style.display = 'block';
		        });
		    }
		});
	</script>
</body>
</html>