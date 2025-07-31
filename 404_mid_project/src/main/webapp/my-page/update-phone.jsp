<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="model.user.UserDto"%>
    <%
    UserDto user = (UserDto) session.getAttribute("user");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/my-page/update-phone.jsp</title>
</head>
<body>
	<h2>전화번호 수정</h2>
	<form action="${pageContext.request.contextPath}/update-phone" method="post">
	<label>새 전화번호 : </label>
	<input type="text" name="phone" value = "<%= user != null ? user.getUsersPhone() : ""%>" />
	<button type="submit">수정하기</button>
	</form>
</body>
</html>