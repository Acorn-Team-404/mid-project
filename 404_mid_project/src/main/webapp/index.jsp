<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
  <style>

    .carousel-inner img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      object-position: center;
    }
  </style>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	
	<jsp:include page="/WEB-INF/include/index-carousel.jsp"></jsp:include>
	
	<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
</body>

</html>