<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/booking/</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-mina/room-info.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-kcr/booking-page.css" />
</head>
<body>
	<jsp:include page="/WEB-INF/include/room-info.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/include/booking-form.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/js/booking/book.js"></script>
	<script src="${pageContext.request.contextPath}/js/booking/info.js"></script>
</body>
</html>