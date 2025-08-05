<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String inputCode = request.getParameter("inputCode");
	String authCode = (String) session.getAttribute("authCode");
	Long createdAt = (Long) session.getAttribute("authCodeCreatedAt");

	if (createdAt == null || System.currentTimeMillis() - createdAt > 180000) {//3분이 지났다면
	    out.print("expired");  // JS에서 "expired"로 처리
	    return;
	}

	if (inputCode != null && inputCode.equals(authCode)) {
	    session.setAttribute("emailVerified", true);
	    session.removeAttribute("authCode");
	    session.removeAttribute("authCodeCreatedAt");
	    out.print("success");
	} else {
	    out.print("fail");
	}
%>
