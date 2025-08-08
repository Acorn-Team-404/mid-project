<%@page import="org.apache.tomcat.jakartaee.commons.lang3.StringUtils"%>
<%@page import="model.room.RoomDao"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.page.PageDto"%>
<%@page import="model.page.PageDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.page.StayDao"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// 검색 keyword 가 있는지 읽어와 본다
	String keyword=request.getParameter("keyword");
	// System.out.println(keyword); // null or "" 또는 "검색어..." 
	if(keyword==null){
		keyword="";
	}
	
	// 기본 페이지 번호는 1로 설정하고
	int pageNum=1;
	// 페이지 번호를 읽어 와서
	String strPageNum=request.getParameter("pageNum");
	// 전달되는 페이지 번호가 있다면
	if(strPageNum != null){
		// 해당 페이지 번호를 숫자로 변경해서 사용한다 
		pageNum=Integer.parseInt(strPageNum);
	}
	
	// 한 페이지에 몇 개씩 표시할 것인지
	final int PAGE_ROW_COUNT=3;
	// 하단 페이지를 몇 개씩 표시할 것인지
	final int PAGE_DISPLAY_COUNT=5;
	
	// 보여줄 페이지 시작 ROWNUM
	int startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT; // 공차수열
	// 보여줄 페이지의 끝 ROWNUM
	int endRowNum=pageNum*PAGE_ROW_COUNT; // 등차수열
	
	// 하단 시작 페이지 번호 
	int startPageNum = 1 + ((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
	// 하단 끝 페이지 번호
	int endPageNum=startPageNum+PAGE_DISPLAY_COUNT-1;
	
	// 전체 글의 갯수
	int totalRow=0;
	// 만일 전달된 keyword 가 없다면
	if(StringUtils.isEmpty(keyword)){
		totalRow=PageDao.getInstance().getCount();
	}else{ // 있다면
		totalRow=PageDao.getInstance().getCountByKeyword(keyword);
	}
	
	// 전체 페이지의 갯수 구하기
	int totalPageCount=(int)Math.ceil(totalRow/(double)PAGE_ROW_COUNT);
	// 끝 페이지 번호가 이미 전체 페이지 갯수보다 크게 계산되었다면 잘못된 값이다
	if(endPageNum > totalPageCount){
		// 보정해 준다
		endPageNum=totalPageCount; // 보정해 준다
	}
	
	String usersId = (String)session.getAttribute("usersId");
	String stayName = (String)session.getAttribute("stayName");
	
	// dto 에 select 할 row 의 정보를 담고
	PageDto dto=new PageDto();
	dto.setPageNum(pageNum);
	dto.setStayName(stayName);
	dto.setStartRowNum(startRowNum);
	dto.setEndRowNum(endRowNum);
	
	// 글 목록
	List<PageDto> list = null;
	// 만일 keyword 가 없으면
	if(StringUtils.isEmpty(keyword)){
		list = PageDao.getInstance().selectPage(dto);
	}else{ // 있다면
		// dto 에 keyword 를 담고
		dto.setKeyword(keyword);
		// 키워드에 해당하는 글 목록을 얻어낸다
		list = PageDao.getInstance().searchPageByKeyword(dto);
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/page-list</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="container py-4">
		<h2>숙소 페이지 목록</h2>
	
		<!-- 검색 폼 -->
		<form action="page-list.jsp" method="get" class="mb-3">
			<div class="input-group">
				<input value="<%=StringUtils.isEmpty(keyword) ? "" : keyword %>" type="text" name="keyword" class="form-control" placeholder="숙소 이름, 위치, 내용 검색">
				<button class="btn btn-primary" type="submit">
					<i class="bi bi-search"></i>
				</button>
			</div>
		</form><br>
		
		<a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/page/page-new-form.jsp" >
			<i class="bi bi-plus-lg"></i> 상세페이지 등록
		</a>
	
		<!--  글 목록 -->
		<div class="row row-cols-1 row-cols-md-3">
			<% for(PageDto tmp : list){ 
				long stayNum = tmp.getStayNum();
			    double avgStar = StayDao.getInstance().getAverageStar(stayNum);
				int reviewCount = StayDao.getInstance().getReviewCount(stayNum);
			%>
			<div class="card-container">
				<div class="card h-100" style="width: 25rem;">
					<!-- 썸네일 이미지가 있는 경우 / list의 0번째 사진 = 썸네일? -->
					<img src="${pageContext.request.contextPath}/page/images/stay01.png" class="card-img-top" alt="<%=tmp.getStayName()%>">
					
					<div class="card-body">
						<h5 class="card-title"><%=tmp.getStayName() %></h5><br>
						<p class="card-text">
							<%=tmp.getStayLoc() %><br>
							<%=String.format("%.1f", avgStar) %> (<%=reviewCount %>)<br>
						</p>
						<a href="${pageContext.request.contextPath}/page/page-view.jsp?pageNum=<%=tmp.getPageNum()%>" class="btn btn-sm btn-primary">상세 보기</a>
					</div>
				</div>
			</div>
			<%} %>
		</div>
			
			<% if(list.size() == 0){ %>
			<div class="col">
				<div class="alert alert-warning text-center w-100">등록된 페이지가 없습니다.</div>
			</div>
			<%} %>
	</div>
		
		<!--  페이징 UI -->
		<ul class="pagination">
			<%if(startPageNum > PAGE_DISPLAY_COUNT){ %>
				<li class="page-item">
					<a class="page-link" href="page-list.jsp?pageNum=<%=startPageNum - PAGE_DISPLAY_COUNT%>&keyword=<%=keyword %>">이전</a>
				</li>
			<%} %>
			<%for(int i = startPageNum; i <= endPageNum; i++){ %>
				<li class="page-item <%= (i == pageNum ? "active" : "") %>">
					<a class="page-link" href="page-list.jsp?pageNum=<%=i%>&keyword=<%=keyword%>"><%=i%></a>
				</li>
			<%} %>
			<%if(endPageNum < totalPageCount){ %>
				<li class="page-item">
					<a class="page-link" href="page-list.jsp?pageNum=<%=startPageNum + PAGE_DISPLAY_COUNT%>&keyword=<%=keyword %>">다음</a>
				</li>
			<%} %>
		</ul>
	</div>
</body>
</html>