<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//GET 방식 파라미터 url 이라는 이름으로 전달되는 값이 있는지 읽어와 본다
	String url=request.getParameter("url");
	//만일 넘어오는 값이 없다면
	if(url==null){
		//로그인 후에 인덱스 페이지로 갈수 있도록 한다. 
		String cPath=request.getContextPath();
		url=cPath+"/index.jsp";
	}	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/user/login-form.jsp</title>
</head>
<body>
	<div class="container">
		<h1>로그인</h1>
		<form action="${pageContext.request.contextPath}/login.user" method="post">
			<%-- 로그인 성공후에 이동할 url 정보를 추가로 form 전송되도록 한다 --%>
			<input type="hidden" name="url" value="<%=url %>"/>
			<div>
				<label for="usersId">아이디</label>
				<input type="text" name="usersId" id="usersId"/>
			</div>
			<div>
				<label for="usersPassword">비밀번호</label>
				<input type="password" name="usersPassword" id="usersPassword"/>
			</div>
			<a href="${pageContext.request.contextPath}/user/findId-form.jsp">아아디 찾기</a>
			<a href="${pageContext.request.contextPath}/user/findpassword-form.jsp">비밀번호 찾기</a>
			<br>
			<button type="submit">로그인</button>
		</form>
	</div>

</body>
</html>