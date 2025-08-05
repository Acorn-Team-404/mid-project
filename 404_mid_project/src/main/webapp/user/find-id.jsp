<%@ page import="model.user.UserDao" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    
    String usersName = request.getParameter("usersName");
    String email = request.getParameter("email");
    
    String findId = UserDao.getInstance().findId(usersName, email);

    if (findId == null) {
%>
    <script>
        alert("일치하는 정보의 아이디가 존재하지 않습니다.");
        history.back();
    </script>
<%
    } else {
%>
    <script>
        alert("<%= usersName %> 님의 아이디는 '<%= findId %>' 입니다.");
        location.href = "<%= request.getContextPath() %>/user/login-form.jsp";
    </script>
<%
    }
%>