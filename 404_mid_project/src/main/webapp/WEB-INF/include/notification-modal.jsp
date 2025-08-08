<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
  	
		<!-- SSE Servlet으로 알림카드가 생성될 body -->
		
  	</div>
</div>

