<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>include/notification-modal.jsp</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification/notification-modal.css" />
</head>
<body>
<div class="offcanvas offcanvas-end" data-bs-scroll="true" tabindex="-1" id="offcanvasWithBothOptions" aria-labelledby="offcanvasWithBothOptionsLabel">
	<div class="offcanvas-header">
    	<h5 class="offcanvas-title ms-3" id="offcanvasWithBothOptionsLabel">
    	<% if (request.getAttribute("usersId") == null) { %>
		    로그인이 필요합니다.
		<% } else { %>
		    <%= request.getAttribute("usersId") %>
		<% } %>
    	</h5>
    	<button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
  	</div>
  	<div class="offcanvas-body">

		<!-- 알림카드가 생성될 body -->
		
  	</div>
</div>

<script>
	const notiModal = document.getElementById("offcanvasWithBothOptions");

	/*notiModal.addEventListener("show.bs.offcanvas", () => {
		if (!window.eventSourceInitialized) {
			initializeSSE();
		} else {
			console.log("⚠️ 이미 SSE 연결 중 - 새 연결 생략");
		}
	});
	*/
	
	
	notiModal.addEventListener("show.bs.offcanvas", () => {
		if (!window.eventSourceInitialized) { // 이미 연결됐는지 확인(중복 연결 방지)
			const offcanvasBody = document.querySelector(".offcanvas-body");
			if (offcanvasBody) offcanvasBody.innerHTML = ""; // 알림 목록 초기화

			initializeSSE();
		} else {
			console.log("⚠️ 이미 SSE 연결 중 - 새 연결 생략");
		}
	});
	
	
	notiModal.addEventListener("hidden.bs.offcanvas", () => {
	    if (window.eventSource) {
	        window.eventSource.close();
	        window.eventSource = null;                 // ✅ 이거 빠지면 메모리에 남아있음
	        window.eventSourceInitialized = false;     // ✅ 이거 없으면 다시 안 연결됨
	        console.log("🔌 SSE 연결 종료됨");
	    }
	});
</script>


<!-- 세션이 있을 때만 sse를 호출하는 js 호출 -->
<%if(request.getAttribute("usersId") != null && session.getAttribute("usersId") != null) {%>
	<script src="${pageContext.request.contextPath}/js/notification/noti-sse.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="${pageContext.request.contextPath}/js/notification/noti-control.js?v=<%= System.currentTimeMillis() %>"></script>
<%} %>

</body>
</html>
