<%@page import="java.net.URLEncoder"%>
<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@page import="model.user.UserDao"%>
<%@page import="model.user.UserDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	
	String usersId = request.getParameter("usersId");
	String usersPassword = request.getParameter("usersPassword");
	String url = request.getParameter("url");
	
	if (url == null) {
		url = request.getContextPath() + "/index.jsp";
	}

	UserDto dto = UserDao.getInstance().getByUserId(usersId);
	boolean isValid = dto != null && dto.getUsersPw() != null 
	                  && BCrypt.checkpw(usersPassword, dto.getUsersPw());

	if (isValid) {
		session.setAttribute("usersId", dto.getUsersId());
		session.setAttribute("usersNum", dto.getUsersNum());
		session.setMaxInactiveInterval(60 * 60); // 1시간 유지
		response.sendRedirect(url);
	} else {
		out.println("<script>alert('입력한 정보와 일치하는 계정이 없습니다.'); history.back();</script>");
	}
%>
