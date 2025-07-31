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
		<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25">
			<!-- 알림용 빨간 점 -->
			<span class="position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
			<div class="noti-img-wrapper ratio ratio-1x1">
				<img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
			</div>
			<div class="noti-text-wrapper d-flex flex-column">
				<div class="d-flex justify-content-between position-relative">
					<div class="d-flex flex-column ps-3 flex-grow-1">
						<span class="text-secondary fs-8">2025.07.02</span>
						<span class="text-semiblack fw-semibold fs-6">제주 재재소소</span>
					</div>
					<div class="noti-type-wrapper position-absolute end-0">
						<span class="text-secondary fs-8">review</span>
					</div>
				</div>
				<hr />
				<div class="noti-message-wrapper d-flex ps-3 justify-content-between">
					<span class="text-semiblack fs-7">신규리뷰</span>
					<span class="text-secondary fs-8">27일 전</span>
				</div>
			</div>
		</div>
		
		
		<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25">
			<!-- 알림용 빨간 점 -->
			<span class="position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
			<div class="noti-img-wrapper ratio ratio-1x1">
				<img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
			</div>
			<div class="noti-text-wrapper d-flex flex-column">
				<div class="d-flex justify-content-between position-relative">
					<div class="d-flex flex-column ps-3 flex-grow-1">
						<span class="text-secondary fs-8">2025.07.02</span>
						<span class="text-semiblack fw-semibold fs-6">제주 재재소소</span>
					</div>
					<div class="noti-type-wrapper position-absolute end-0">
						<span class="text-secondary fs-8">review</span>
					</div>
				</div>
				<hr />
				<div class="noti-message-wrapper d-flex ps-3 justify-content-between">
					<span class="text-semiblack fs-7">신규리뷰</span>
					<span class="text-secondary fs-8">27일 전</span>
				</div>
			</div>
		</div>
		
		
  	</div>
</div>
</body>
</html>