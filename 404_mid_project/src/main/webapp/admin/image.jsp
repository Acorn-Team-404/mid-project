<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/admin/image.jsp</title>
</head>
<body>
  <h3>🖼 NAS 이미지 업로드</h3>
  <form action="${pageContext.request.contextPath}/uploadToNAS" method="post" enctype="multipart/form-data">
    <label>파일 선택: </label>
    <input type="file" name="uploadFile" required>
    <button type="submit">업로드</button>
  </form>
</body>
</html>