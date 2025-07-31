<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
  <style>
/*     .carousel-inner {
      height: 600px;
    } */
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
		<div id="carouselExampleIndicators" class="carousel slide">
		  <div class="carousel-indicators">
		    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
		    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
		    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
		  </div>
		  
		  <div class="carousel-inner ratio ratio-16x9">
		    <div class="carousel-item active">
		      <img src="${pageContext.request.contextPath}/images/indexc01.jpg" class="d-block w-100" alt="...">
		    </div>
		    <div class="carousel-item">
		      <img src="${pageContext.request.contextPath}/images/indexc02.jpeg" class="d-block w-100" alt="...">
		    </div>
		    <div class="carousel-item">
		      <img src="${pageContext.request.contextPath}/images/indexc03.jpg" class="d-block w-100" alt="...">
		    </div>
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
	<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
</body>
</html>