<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- 알람 모달창 -->
	<div class="modal fade" id="alertModal" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
	    	<div class="modal-content">
	    		<div class="modal-header no-border">
	       			<h5 class="modal-title">알림</h5>
	        		<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	      		</div>
	      		<div class="modal-body" id="alertModalBody">
	        		<!-- 메시지 삽입 -->
	      		</div>
	      		<div class="modal-footer no-border">
	        		<button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
	      		</div>
	    		</div>
	  	</div>
	</div>
</body>
</html>