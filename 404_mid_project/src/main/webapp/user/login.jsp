<%@page import="java.net.URLEncoder"%>
<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@page import="model.user.UserDao"%>
<%@page import="model.user.UserDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String url = request.getParameter("url");	
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	
	String usersId = request.getParameter("usersId");
	String usersPassword = request.getParameter("usersPassword");
	
	
	if (url == null || url.contains("loginform.jsp")) {
		url = request.getContextPath() + "/index.jsp";
	}

	UserDto dto = UserDao.getInstance().getByUserId(usersId);
	boolean isValid = dto != null && dto.getUsersPw() != null 
	                  && BCrypt.checkpw(usersPassword, dto.getUsersPw());

	if (isValid) {
		session.setAttribute("usersId", dto.getUsersId());
		session.setAttribute("usersNum", dto.getUsersNum());
		session.setAttribute("usersRole", dto.getUsersRole()); //role 정보
		System.out.println("DB에서 읽은 사용자 역할: " + dto.getUsersRole());
		session.setMaxInactiveInterval(60 * 60); // 1시간 유지
		
		String isSave=request.getParameter("isSave");
		
		if(isSave != null){
			Cookie cook=new Cookie("savedUserId", usersId);
			cook.setMaxAge(60*60*24*7); 
			response.addCookie(cook);
		}else{		
			Cookie cook=new Cookie("savedUserId", "");

			cook.setMaxAge(0);
			response.addCookie(cook);
		}
		
		response.sendRedirect(url);
		
	} else {
		request.setAttribute("modalMessage", "입력한 정보와 일치하는 계정이 없습니다.");
		request.setAttribute("goBack", true); // 모달 닫은 후 뒤로 가기
		request.setAttribute("isValid", false); // 모달 닫은 후 뒤로 가기
		RequestDispatcher rd = request.getRequestDispatcher("/user/login-form.jsp");
		rd.forward(request, response);
		return;
	}
	
	
	
%>
