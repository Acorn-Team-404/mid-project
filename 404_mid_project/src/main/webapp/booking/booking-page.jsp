<%@page import="model.image.ImageDao"%>
<%@page import="model.image.ImageDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String usersId = (String) request.getAttribute("usersId");
		Long stayNum = Long.parseLong(request.getParameter("stayNum"));
    /* if (usersId == null) {
        response.sendRedirect(request.getContextPath() + "/booking/submit");
        return;
    } */
    List<ImageDto> carouselImages = ImageDao.getInstance().getListByTargetLong("stay", stayNum);
    int size = carouselImages.size();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/booking/</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-mina/room-info.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-kcr/booking-page.css" />
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />
	<% if (size > 0) { %>
	<div id="carouselExampleIndicators" class="carousel slide">
	  <div class="carousel-indicators">
	    <% for (int i = 0; i < size; i++) { %>
	      <button type="button"
	              data-bs-target="#carouselExampleIndicators"
	              data-bs-slide-to="<%= i %>"
	              class="<%= (i == 0) ? "active" : "" %>"
	              aria-current="<%= (i == 0) ? "true" : "" %>"
	              aria-label="Slide <%= i + 1 %>"></button>
	    <% } %>
	  </div>
	
	  <div class="carousel-inner ratio ratio-21x9">
	    <% for (int i = 0; i < size; i++) {
	         ImageDto img = carouselImages.get(i);
	    %>
	      <div class="carousel-item <%= (i == 0) ? "active" : "" %>">
	        <img src="<%= request.getContextPath() %>/show.img?imageName=<%= img.getImageSavedName() %>"
					     class="d-block w-100"
					     alt="<%= img.getImageOriginalName() %>">
	      </div>
	    <% } %>
	  </div>
	
	  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
	    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
	    <span class="visually-hidden">Previous</span>
	  </button>
	  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
	    <span class="carousel-control-next-icon" aria-hidden="true"></span>
	    <span class="visually-hidden">Next</span>
	  </button>
	</div>
	<% } else { %>
	  <p>등록된 이미지가 없습니다.</p>
	<% } %>
	
	<%-- <input type="hidden" name="bookStayNum" value="${stay.stayNum}"/> --%>
	<jsp:include page="/WEB-INF/include/room-info.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/include/booking-form.jsp"></jsp:include>

</body>
</html>