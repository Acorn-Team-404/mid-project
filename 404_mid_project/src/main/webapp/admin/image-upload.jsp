<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>이미지 업로드 테스트</title>
  <jsp:include page="/WEB-INF/include/resource.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/image-upload-form.css" />
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />
<jsp:include page="/WEB-INF/include/image-upload-form.jsp" />

<script src="${pageContext.request.contextPath}/js/admin/image-upload-form.js"></script>
</body>
</html>
