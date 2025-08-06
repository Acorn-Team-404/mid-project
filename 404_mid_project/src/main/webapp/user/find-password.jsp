<%@ page import="model.user.UserDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/include/user-modal.jsp" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
<%
    request.setCharacterEncoding("UTF-8");//폼 데이터를 POST로 받을 때는 반드시 필요

    String usersName = request.getParameter("usersName");
    String usersId = request.getParameter("usersId");
    String usersEmail = request.getParameter("usersEmail");

    boolean isValid = UserDao.getInstance().findPassword(usersName, usersId, usersEmail);

    if (isValid) {
    	//비밀번호 재설정
        session.setAttribute("resetPasswordId", usersId);
        response.sendRedirect("reset-password-form.jsp");
        
    } else {
%>
	<script>
	function showInfoModal(message) {
	  document.getElementById("modalMessage").textContent = message;
	  const modal = new bootstrap.Modal(document.getElementById("infoModal"));
	  modal.show();
	}
	
	document.addEventListener("DOMContentLoaded", function () {
	  showInfoModal("입력한 정보와 일치하는 계정이 없습니다.");
	  document.getElementById("infoModal").addEventListener("hidden.bs.modal", function () {
	    history.back();
	  });
	});
	</script>
<%
    }
%>