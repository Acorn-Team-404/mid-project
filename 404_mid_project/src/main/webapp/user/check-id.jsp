<%@page import="model.user.UserDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

		
	String usersId = request.getParameter("usersId");
	
	if (usersId == null || usersId.trim().isEmpty()) {
	    response.getWriter().print("empty");
	    return;
	}
	
	boolean isIdExist = UserDao.getInstance().isIdExist(usersId);
	response.getWriter().print(isIdExist ? "exist" : "ok");
	

%>
