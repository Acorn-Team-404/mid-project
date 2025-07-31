<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>include/notification-modal.jsp</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification-modal.css" />
</head>
<body>
<div class="offcanvas offcanvas-end" data-bs-scroll="true" tabindex="-1" id="offcanvasWithBothOptions" aria-labelledby="offcanvasWithBothOptionsLabel">
	<div class="offcanvas-header">
    	<h5 class="offcanvas-title" id="offcanvasWithBothOptionsLabel">infreeJ</h5>
    	<button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
  	</div>
  	<div class="offcanvas-body">
		<!-- 알림 카드 (읽지 않은 경우 .unread 추가) -->
		<div class="noti-card">
			<img src="" alt="" />
			<span>2025.07.02</span>
			<span>제주 재재소소</span>
			
		</div>
  	</div>
</div>
</body>
</html>