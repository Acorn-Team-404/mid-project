<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    List<PostDto> list = (List<PostDto>) request.getAttribute("list");
        if (list == null) list = new ArrayList<>();
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
		<jsp:param value="board" name="thisPage"/>	
	</jsp:include>
	
	<div class="container">
		<div> 
			<h4 class="text-center" style="margin-top: 50px; margin-bottom: 100px;">JOURNAL</h4>
			<a class="btn btn-sm position-absolute end-0 me-5" 
				href="form.jsp" >
				post
			<i class="bi bi-pencil-square"></i>
			</a>
		</div>
		
		<ul class="row list-unstyled">
		<%
		for(PostDto post : list) {
		%>
			<li class="col-md-4 mb-4">
				<div class="card position-relative">
					<a href="view.post?num=<%=post.getPostNum() %>"><img src="${pageContext.request.contextPath}/post/images/seoul.jpg" class="card-img-top"></a>
					
					<div class="position-absolute top-50 start-50 translate-middle w-100 bg-dark bg-opacity-50 text-white text-center px-3 py-2">
						<%=post.getPostTitle() %>
					</div>
				</div>
			</li>
		<% } %>
			
			
		</ul>
		

	</div> <!-- container -->
</body>
</html>