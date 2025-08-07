<%@ page import="model.user.UserDao" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/include/user-modal.jsp" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
<%
    request.setCharacterEncoding("UTF-8");
    
    String usersName = request.getParameter("usersName");
    String email = request.getParameter("email");
    
    String findId = UserDao.getInstance().findId(usersName, email);

    if (findId == null) {
%>
	<script>
	document.addEventListener("DOMContentLoaded", function () {
	    document.getElementById("modalMessage").textContent = "일치하는 정보의 아이디가 존재하지 않습니다.";
	    const modal = new bootstrap.Modal(document.getElementById("infoModal"));
	    modal.show();
	
	    document.getElementById("infoModal").addEventListener("hidden.bs.modal", function () {
	        history.back();
	    });
	});
	</script>
<%
    } else {
%>
    <script>
    	document.addEventListener("DOMContentLoaded", function () {
    		document.getElementById("modalMessage").textContent = "<%=usersName%>님의 아이디는 <%=findId%> 입니다.";
    	    const modal = new bootstrap.Modal(document.getElementById("infoModal"));
    	    modal.show();
    	
    	    document.getElementById("infoModal").addEventListener("hidden.bs.modal", function () {
    	        location.href = "<%= request.getContextPath() %>/user/login-form.jsp";
    	    });
    	});
    </script>
<%
    }
%>