<%@page import="model.image.ImageDto"%>
<%@page import="model.image.ImageDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="row">
	<% for (int i = 1; i <= 3; i++) {
		ImageDto imageDto = ImageDao.getInstance().selectByIntSingleImage("room", i);
		String imageName = imageDto.getImageSavedName();
	%>
		<div class="col-4">
			<img src="<%= request.getContextPath() %>/show.img?imageName=<%= imageName %>" alt="객실 <%= i %>" style="width:50%; height:auto;" />
		</div>
	<% } %>
	</div>
</body>
</html>