<%@page import="model.util.MailAsync"%>
<%@page import="model.util.MailUtil"%>
<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String usersEmail = request.getParameter("usersEmail");
	String authCode = String.valueOf((int)(Math.random() * 900000) + 100000);

	try {
	    session.setAttribute("authCode", authCode);
	    session.setAttribute("authCodeCreatedAt", System.currentTimeMillis());
	    session.setAttribute("emailVerified", false);
	    
	    MailAsync.sendAsync(usersEmail, authCode);//메일을 백그라운드에서 보낸다. 
	    
	    out.print("success");
	} catch (Exception e) {
	    out.print("fail");
	}
%>
