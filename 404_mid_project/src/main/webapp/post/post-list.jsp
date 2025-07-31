<%@page import="model.post.postDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.post.postDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	postDTO dto=new postDTO();
	List<postDTO> list=new postDAO().selectAll();
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/post/post-list.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp">
		<jsp:param value="board" name="thisPage"/>	
	</jsp:include>
	
	<div class="container">
		
		<h4>JOURNAL</h4>
		<a class="btn btn-sm" href="getBoard.do" >
		post
		<i class="bi bi-pencil-square"></i>
		</a>
		
		<ul class="row list-unstyled">
		<% for(postDTO post : list) { %>
			<li class="col-md-4 mb-4">
				<div class="card">
					<img src="/images/stay (1).webp" class="card-img-top">
					<div class="card-body">
						<p class="card-title mb-2"><%=post.getPost_title() %></p>
					</div>
				</div>
			</li>
		<% } %>
			
			
		</ul>
		

	</div> <!-- container -->
</body>
</html>