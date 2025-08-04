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
	<jsp:include page="/WEB-INF/include/navbar.jsp" />
	<div class="container my-5">
		<form action="${pageContext.request.contextPath}/booking/submit" method="post" id="bookingForm">
		  <div class="row g-5">
		<jsp:include page="/WEB-INF/include/room-info2.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/include/booking-form.jsp"></jsp:include>
			</div>
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/js/booking/book.js"></script>
	<script src="${pageContext.request.contextPath}/js/booking/info.js"></script>
</body>
</html>