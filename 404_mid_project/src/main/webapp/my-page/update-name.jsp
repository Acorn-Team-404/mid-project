<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.user.UserDto" %>
	<%
  UserDto user = (UserDto) session.getAttribute("user"); // 세션에 user가 없으면 my-page 서블릿에서 setAttribute 해줘야 함
	%>
<!DOCTYPE html>
<html>
<head>
<title>/my-page/update-name.jsp</title>
</head>
<body>
  <h2>이름 수정</h2>
  <form action="${pageContext.request.contextPath}/update-name" method="post">
    <label>새 이름: </label>
    <input type="text" name="usersName" value="<%= user != null ? user.getUsersName() : "" %>" />
    <button type="submit">수정하기</button>
  </form>
</body>
</html>
