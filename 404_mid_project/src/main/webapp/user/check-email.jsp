<%@page import="model.user.UserDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    String email = request.getParameter("usersEmail");
    boolean isExist = UserDao.getInstance().isEmailExist(email);
    out.print(isExist ? "exist" : "available");
%>
