<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>user/findid.form</title>
</head>
<body>
	<div class="container">
		<h1>아이디 찾기</h1>
		<form action="${pageContext.request.contextPath}/findId.user" method="get">
			<div>
				<label for="usersName">이름</label>
				<input type="text" name="usersName" id="usersName"/>
			</div>
			<div>
				<label for="email">이메일</label>
				<input type="text" name="email" id="email"/>
			</div>
			<button type="submit">조회하기</button>
		</form>
	</div>
	
</body>
</html>