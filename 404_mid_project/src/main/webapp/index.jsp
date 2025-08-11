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
    
    .listing-card {
		  max-width: 18rem;
		  border-radius: .75rem;
		  overflow: hidden;
		}
		
		.listing-card .ratio {
		  border-top-left-radius: .75rem;
		  border-top-right-radius: .75rem;
		}
		
		.listing-card .card-body {
		  padding-left: .5rem;
		  padding-right: .5rem;
		}
    
  </style>
</head> 
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	
	<jsp:include page="/WEB-INF/include/index-stay-card.jsp" />
	<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
	
</body>

</html>