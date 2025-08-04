<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String usersId = (String) request.getAttribute("usersId");
    /* if (usersId == null) {
        response.sendRedirect(request.getContextPath() + "/booking/submit");
        return;
    } */
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
	<!-- <input type="hidden" name="bookStayNum" value="\${stay.stayNum}" /> -->
	<%-- <input type="hidden" name="bookStayNum" id="bookStayNum" value="${stay.stayNum}" /> --%>
	
	<input type="text" name="bookStayNum" value="100"/>
	<jsp:include page="/WEB-INF/include/room-info.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/include/booking-form.jsp"></jsp:include>
</body>
</html>