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
			<span class="noti-dott d-inline-block rounded-circle bg-danger" style="width: 10px; height: 10px;"></span>
			<img class="noti-img" src="" alt="" />
			<div class="noti-top-wrapper">
				<div class="noti-title-wrapper">
					<span>2025.07.02</span>
					<span>제주 재재소소</span>
				</div>
				<div class="noti-type-wrapper">
					<span>review</span>
				</div>
			</div>
			<hr />
			<div class="noti-message-wrapper">
				<span>신규리뷰</span>
				<span>27일 전</span>
			</div>
		</div>
  	</div>
</div>
</body>
</html>