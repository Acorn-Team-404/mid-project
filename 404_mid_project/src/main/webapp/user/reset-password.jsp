<%@page import="model.user.UserDao"%>
<%@page import="model.user.UserDto"%>
<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%! 
	public void alertAndBack(JspWriter out, String msg) throws java.io.IOException {
	    out.println("<script>alert('" + msg + "'); history.back();</script>");
	} 
%>
    
<%	
	// 파라미터 추출
	String usersId = (String)request.getSession().getAttribute("resetPasswordId");
	System.out.println(usersId);
	String newUsersPassword = request.getParameter("newUsersPassword");
	String newUsersCheckPassword = request.getParameter("newUsersCheckPassword");
	
	
	// 비밀번호 유효성 검사
	if (newUsersPassword == null || !newUsersPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,}$")) {
		alertAndBack(out, "비밀번호 형식이 올바르지 않습니다. (대소문자+숫자+특수문자 포함 10자 이상)");
		return;
	}
	
	// 비밀번호 확인 일치 여부
	if (newUsersCheckPassword == null || !newUsersPassword.equals(newUsersCheckPassword)) {
		alertAndBack(out, "비밀번호가 일치하지 않습니다.");
		return;
	}
	
	
	
	// 비밀번호 암호화
	String hashed = BCrypt.hashpw(newUsersPassword, BCrypt.gensalt());
	
	// DTO 생성
	UserDto dto = new UserDto();
	dto.setUsersPw(hashed);
	
	// DB에 insert
	boolean isSuccess = UserDao.getInstance().updateUserPassword(usersId, hashed);
	
	
	if (isSuccess) {
		request.getSession().removeAttribute("resetPasswordId");
		response.sendRedirect(request.getContextPath() + "/user/login-form.jsp");
		return;
	} else {
		alertAndBack(out, "비밀번호 변경 실패! 다시 시도해주세요.");
	}			    
%>

