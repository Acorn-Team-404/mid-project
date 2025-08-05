<%@page import="model.util.MailUtil"%>
<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String usersEmail = request.getParameter("usersEmail");
	String authCode = String.valueOf((int)(Math.random() * 900000) + 100000);

	try {
	    MailUtil.sendEmail(usersEmail, authCode); // 이메일 전송
	    session.setAttribute("authCode", authCode);
	    session.setAttribute("authCodeCreatedAt", System.currentTimeMillis());
	    session.setAttribute("emailVerified", false);
	    out.print("success");
	} catch (Exception e) {
	    out.print("fail");
	}
%>
