<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>users/findpassword-form.jsp</title>
</head>
<body>
	<div class="container">
		<h1>비밀번호 찾기</h1>
		<form action="${pageContext.request.contextPath}/findPassword.user" method="post">
			<div>
				<label for="usersName">이름</label>
				<input type="text" name="usersName" id="usersName"/>
			</div>
			<div>
				<label for="usersId">아이디</label>
				<input type="text" name="usersId" id="usersId"/>
			</div>
			<div>
				<label for="usersEamil">이메일</label>
				<input type="email" name="usersEmail" id="usersEmail"/>
			</div>
			<button type="submit">비밀번호 재설정하기</button>
		</form>
	
	</div>
</body>
</html>