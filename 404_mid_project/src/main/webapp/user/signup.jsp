<%@page import="model.user.UserDao"%>
<%@page import="model.user.UserDto"%>
<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%! 
	public void alertAndBack(JspWriter out, String msg) throws java.io.IOException {
	    out.println("<script>alert('" + msg + "'); history.back();</script>");
	} 
%>

<%
	Boolean verified = (Boolean) session.getAttribute("emailVerified");
	if (verified == null || !verified) {
	    alertAndBack(out, "이메일 인증을 완료해주세요.");
	    return;
	}

	String usersId = request.getParameter("usersId");
	String usersName = request.getParameter("usersName");
	String usersPassword = request.getParameter("usersPassword");
	String usersCheckPassword = request.getParameter("usersCheckPassword");
	String email = request.getParameter("email");
	String phone = request.getParameter("phone");
	String birth = request.getParameter("birth");
	String checkIdAction = request.getParameter("checkIdAction");

	if (usersId == null || !usersId.matches("^[a-zA-Z0-9]{4,20}$")) {
		alertAndBack(out, "아이디 형식이 올바르지 않습니다.");
		return;
	}
	if (usersPassword == null || !usersPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,}$")) {
		alertAndBack(out, "비밀번호 형식이 올바르지 않습니다.");
		return;
	}
	if (usersCheckPassword == null || !usersPassword.equals(usersCheckPassword)) {
		alertAndBack(out, "비밀번호가 일치하지 않습니다.");
		return;
	}
	if (!"on".equals(checkIdAction)) {
		alertAndBack(out, "아이디 중복확인을 해주세요.");
		return;
	}

	String hashed = BCrypt.hashpw(usersPassword, BCrypt.gensalt());

	UserDto dto = new UserDto();
	dto.setUsersId(usersId);
	dto.setUsersName(usersName);
	dto.setUsersPw(hashed);
	dto.setUsersEmail(email);
	dto.setUsersPhone(phone);
	dto.setUsersBirth(birth);

	boolean isSuccess = UserDao.getInstance().insert(dto);

	if (isSuccess) {
%>
	<script>
		alert("회원가입이 완료되었습니다.");
		location.href="<%=request.getContextPath() %>/index.jsp";
	</script>
<%
	} else {
		alertAndBack(out, "회원가입 실패! 다시 시도해주세요.");
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>user/signup.jsp</title>
</head>
<body>

</body>
</html>