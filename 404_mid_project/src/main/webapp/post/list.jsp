<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    List<PostDto> list = PostDao.getInstance().selectAll();
        if (list == null) list = new ArrayList<>();
        
    PostDto dto=new PostDto();
	    
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
	
	<div class="container">
		<div> 
			<h2 class="text-center" style="margin-top: 100px; margin-bottom: 100px;">JOURNAL</h2>
			<a class="btn btn-sm position-absolute end-0 me-5" 
				href="${pageContext.request.contextPath}/post/form.jsp" >
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
					<a href="${pageContext.request.contextPath}/post/view.post?num=<%=post.getPostNum() %>"><img src="${pageContext.request.contextPath}/images/indexc01.jpg" class="card-img-top"></a>
					
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