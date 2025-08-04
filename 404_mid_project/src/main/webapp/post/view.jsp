<%@page import="model.post.CommentDto"%>
<%@page import="java.util.List"%>
<%@page import="model.post.CommentDao"%>
<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int num=Integer.parseInt(request.getParameter("num"));
	
	PostDto dto = PostDao.getInstance().getByPostNum(num);
	//List<PostImageDto> images = PostDao.getInstance().getImageList(num);
	List<CommentDto> commentList = CommentDao.getInstance().selectAll(num);
	String usersNum=(String)session.getAttribute("usersNum");
	boolean isLogin = usersNum == null ? false : true;
	/* if(dto.getPostWriterNum != usersNum){
		PostDao.getInstance().addViews(num);
		} 
	*/
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/post/view.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp">
		<jsp:param value="view" name="thisPage"/>	
	</jsp:include>
	
	<!-- 이미지..경로 필요!!! -->
	<jsp:include page="/WEB-INF/include/index-carousel.jsp"></jsp:include>
	
	<div class="container">
	
		<div>
			<!-- 제목 -->
			
			<h1 class="text-center my-4"><%=dto.getPostTitle() %></h1>
		
		</div>
		
		<!-- 본문 -->
		<div class="card-body">
			<p class="card-text"><%=dto.getPostContent().replaceAll("\n", "<br>") %></p>
		</div>
		
		<div class="container col-6 mt-5">
			
			
			</div>

			<div class="container col-10 mt-5">

				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">글 번호</div>
				  <div class="col-6 text-end"><%= dto.getPostNum() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">작성자</div>
				  <div class="col-6 text-end"><%= dto.getPostWriterNum() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">숙소 번호</div>
				  <div class="col-6 text-end"><%= dto.getPostStayNum() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">조회수</div>
				  <div class="col-6 text-end"><%= dto.getPostViews() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">작성일</div>
				  <div class="col-6 text-end"><%= dto.getPostCreatedAt() %></div>
				</div>
				
				<!-- 숨기고 싶은 값 -->
				<div class="d-none">
				  <%= dto.getPostType() %>
				  </div>
				
				</div>
			

		</div>
	
	</div>
</body>
</html>