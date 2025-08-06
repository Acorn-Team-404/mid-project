<%@page import="org.apache.tomcat.jakartaee.commons.lang3.StringUtils"%>
<%@page import="oracle.jdbc.proxy.annotation.Post"%>
<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	//검색 keyword가 있는지 읽어와보기
	String keyword=request.getParameter("keyword");
	System.out.println(keyword); //null or "" 또는 "검색어"
	
	if(keyword==null){
		keyword="";
	}
	// 기본 1페이지
	int pageNum=1;
	String strPageNum=request.getParameter("pageNum");
	//전달되는 페이지 번호 있다면
	if(strPageNum != null){
		//해당 페이지 번호를 숫자로 변경해서 사용한다.
		pageNum=Integer.parseInt(strPageNum);
	}
	
	// 한 페이지에 몇개 표시?
	final int PAGE_ROW_COUNT=9;
	//하단 페이지를 몇개 표시할건지
	final int PAGE_DISPLAY_COUNT=5;
	
	//보여줄 페이지 시작 ROWNUM
	int startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT; //공차수열
	//보여줄 페이지의 끝 ROWNUM
	int endRowNum=pageNum*PAGE_ROW_COUNT; //등비수열
	
	//하단 시작 페이지 번호 (소수점이 버려진 정수)를 이용
	int startPageNum = 1+ ((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
	//하단 끝 페이지 번호
	int endPageNum = startPageNum+PAGE_DISPLAY_COUNT-1;
	
	//전체 글의 갯수
	int totalRow=0;
	if(StringUtils.isEmpty(keyword)){
		totalRow=PostDao.getInstance().getPostCount();
	}else{
		totalRow=PostDao.getInstance().getCountByKeyword(keyword);
	};
	//전체 페이지 갯수
	int totalPageCount=(int)Math.ceil(totalRow/(double)PAGE_ROW_COUNT);
	//끝 페이지 번호가 이미 전체 페이지 갯수보다 크게 계산되었다면 잘못된 값이다.
	if(endPageNum > totalPageCount){
		endPageNum=totalPageCount; //보정
	}
	
	//dto에 select할 row의 정보 담기
	PostDto dto=new PostDto();
	dto.setStartRowNum(startRowNum);
	dto.setEndRowNum(endRowNum);
	//글목록
	List<PostDto> list=null;
	//만일 keyword 없으면
	if(StringUtils.isEmpty(keyword)){
		list=PostDao.getInstance().selectPage(dto);
	}else{
		dto.setPostKeyword(keyword);
		list=PostDao.getInstance().selectPageByKeyword(dto);
	}
		
    //List<PostDto> list = PostDao.getInstance().selectAll();
    //if (list == null) list = new ArrayList<>();
        
    // ✅ 콘솔 디버깅용 로그 찍기
	System.out.println("총 게시글 수: " + totalRow);
	System.out.println("시작 페이지 번호: " + startPageNum);
	System.out.println("끝 페이지 번호: " + endPageNum);
	System.out.println("전체 페이지 수: " + totalPageCount);
	    
    %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/post/list.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp">
		<jsp:param value="post" name="thisPage"/>	
	</jsp:include>
	
	<div class="container mb-5">
		<div> 
			<h2 class="text-center" style="margin-top: 80px; margin-bottom: 50px;">JOURNAL</h2>
			<div class="d-flex justify-content-end mb-4">
			<a class="btn btn-sm btn-outline-secondary" 
				href="${pageContext.request.contextPath}/post/form.jsp" >
				post
			<i class="bi bi-pencil-square"></i>
			</a>
			</div>
		</div>
		
		<ul class="row list-unstyled">
		<%
		for(PostDto post : list) {
		%>
			<li class="col-md-4 mb-4">
				<div class="card position-relative">
					<a href="${pageContext.request.contextPath}/post/view.post?num=<%=post.getPostNum() %>"><img src="${pageContext.request.contextPath}/images/indexc01.jpg" class="card-img-top"></a>
					
					<div class="position-absolute top-50 start-50 translate-middle w-100 bg-dark bg-opacity-50 text-white text-center px-3 py-2">
						<%=post.getPostTitle() %>
					</div>
				</div>
			</li>
		<% } %>
			
			
		</ul>
		
		<ul class="pagination">
			<%-- startPageNum 이 1이 아닐때 이전 page 가 존재하기 때문에... --%>
			<%if(startPageNum != 1){ %>
				<li class="page-item">
					<a class="page-link" href="list.jsp?pageNum=<%=startPageNum-1 %>&keyword=<%=keyword %>">&lsaquo;</a>
				</li>
			<%} %>			
			<%for(int i=startPageNum; i<=endPageNum ; i++){ %>
				<li class="page-item <%= i==pageNum ? "active":"" %>">
					<a class="page-link" href="list.jsp?pageNum=<%=i %>&keyword=<%=keyword %>"><%=i %></a>
				</li>
			<%} %>
			<%-- endPageNum 이 totalPageCount 보다 작을때 다음 page 가 있다 --%>		
			<%if(endPageNum < totalPageCount){ %>
				<li class="page-item">
					<a class="page-link" href="list.jsp?pageNum=<%=endPageNum+1 %>&keyword=<%=keyword %>">&rsaquo;</a>
				</li>
			<%} %>	

			
		</ul>
	</div> <!-- container -->
</body>
</html>