<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int num=Integer.parseInt(request.getParameter("num"));

	PostDto dto = PostDao.getInstance().getByPostNum(num);
	String usersNum=(String)session.getAttribute("usersNum");
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
	<div class="container">
		<h1><%=dto.getPostTitle() %></h1>
	
	</div>
</body>
</html>