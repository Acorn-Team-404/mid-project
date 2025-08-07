<%@page import="java.util.List"%>
<%@page import="model.review.ReviewDao"%>
<%@page import="model.review.ReviewDto"%>
<%@page import="model.book.GuidelineDao"%>
<%@page import="model.book.GuidelineDto"%>
<%@page import="model.page.PageDao"%>
<%@page import="model.page.PageDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	long pageNum=Long.parseLong(request.getParameter("pageNum"));
	PageDto pageDto = PageDao.getInstance().getByNum(pageNum);
	
	long guideId = 1; //숙소별 가이드라인으로 변경되면 수정
	GuidelineDto guide = GuidelineDao.getInstance().getByGuideId(guideId);
	
	long stayNum=pageDto.getStayNum();
	
	long rPageNum=1; //리뷰 페이지 번호
	String strPageNum=request.getParameter("rPageNum");
	if(strPageNum != null){
		rPageNum=Long.parseLong(strPageNum);
	}
	
	//한 페이지에 몇개씩 표시할 것인지
	final long PAGE_ROW_COUNT=3;
		
	//하단 페이지를 몇개씩 표시할 것인지
	final long PAGE_DISPLAY_COUNT=7;
	
	//보여줄 페이지의 시작,끝 ROWNUM
	long startRowNum=1+(rPageNum-1)*PAGE_ROW_COUNT;
	long endRowNum=rPageNum*PAGE_ROW_COUNT;
	
	//하단 시작,끝 페이지 번호
	long startPageNum = 1 + ((rPageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
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
</style>
</head>
<body>
	<!-- Carousel 영역 -->
	
	<div class="sticky-top">
		<div class="full-width-bg bg-light">
			<div class="container d-flex justify-content-between align-items-center bg-light">
				<div>
					<p class="fs-5 mb-0"><%=pageDto.getStayName() %></p>
					<p><%=pageDto.getStayLoc().replaceAll("\n", "<br>") %></p>
				</div>
				<div>
					<a class="btn btn-sm btn-primary me-2" href="${pageContext.request.contextPath }/booking/submit?stayNum=<%=stayNum %>">예약하기</a>
					<a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath }/inquiry/new-inquiry.jsp?stayNum=<%=stayNum %>">문의하기</a>
				</div>
			</div>
		</div>
		<div class="bg-white">
			<ul class="nav nav-tabs justify-content-center border-0">
				<li class="nav-item">
					<a class="nav-link" href="#content-section">소개</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#review-section">리뷰</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#location-section">위치정보</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#guide-section">안내사항</a>
				</li>
			</ul>	
		</div>
	</div>
	<div class="container">
		<div>
			<div class="section section-line mt-2" id="content-section"> <!-- 소개글 -->
				<p class="section-title">소개글</p>
				<div class="fs-7"><%=pageDto.getPageContent().replaceAll("\n", "<br>") %></div>
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
			        <p class="fs-7"><strong>주소:</strong> <%=pageDto.getStayAddr()%></p>
			    </div>
			    <!-- 지도 include -->
			    <%
				    // include할 JSP에 데이터 전달
				    request.setAttribute("stayAddress", pageDto.getStayAddr());
				    request.setAttribute("stayName", pageDto.getStayName());
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
</body>
</html>