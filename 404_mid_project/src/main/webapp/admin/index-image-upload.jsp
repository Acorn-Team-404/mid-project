<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
		request.setAttribute("imageAction", "index"); // index용 업로드 지정
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Index 이미지 업로드</title>
  <jsp:include page="/WEB-INF/include/resource.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/image-upload-form.css" />
</head>
</head>
<body>
  <jsp:include page="/WEB-INF/include/navbar.jsp" />

  <div class="container mt-4">
    <h2><i class="bi bi-image"></i> Index 이미지 업로드</h2>
    
    <!-- 업로드 폼 include -->
    <jsp:include page="/WEB-INF/include/image-upload-form.jsp" />
  </div>

	<script src="${pageContext.request.contextPath}/js/admin/image-upload-form.js"></script>
</body>
</html>
