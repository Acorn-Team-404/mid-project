<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<title>users/findpassword-form.jsp</title>
<style>
	.custom-btn {
		background-color: #d5d5d5;
	    border: none;
	    color: black;
	    padding: 10px 16px;
	    font-size: 16px;
	    border-radius: 8px;
	    transition: background-color 0.3s;
	 }
	
	.custom-btn:hover {
		background-color: #c1c2c1;
	 }
</style>
</head>
<body>
	<h1 class="text-center mt-4">비밀번호 찾기</h1>
	<div class="container mt-5 mb-5">
		<form action="${pageContext.request.contextPath}/user/find-password.jsp" method="post">
			<div class="mb-4">
				<label for="usersName" class="form-label">이름</label>
				<input type="text" name="usersName" class="form-control w-80" id="usersName"/>
			</div>
			<div class="mb-4">
				<label for="usersId" class="form-label">아이디</label>
				<input type="text" name="usersId" class="form-control w-80" id="usersId"/>
			</div class="mb-4">
			<div class="mb-4">
				<label for="usersEamil" class="form-label">이메일</label>
				<input type="email" name="usersEmail" class="form-control w-80" id="usersEmail"/>
			</div>
			<button type="submit" class="btn custom-btn d-block mx-auto">비밀번호 재설정하기</button>
		</form>
	
	</div>
</body>
</html>