<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<title>user/reset-password-form.jsp</title>
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
	<h1 class="text-center mt-4">비밀번호 재설정</h1>
	<div class="container mt-5 mb-5">
		<form action="${pageContext.request.contextPath}/user/reset-password.jsp" method="post">
			<div class="mb-4">
				<label for="newUsersPassword" class="form-label">비밀번호</label>
				<input type="password" name="newUsersPassword" class="form-control w-80" id="newUsersPassword" required>
				<span id="usersPasswordMessage"></span>
			</div>
			
			<div class="mb-4">
				<label for="newUsersCheckPassword" class="form-label">비밀번호 확인</label>
				<input type="password" name="newUsersCheckPassword" class="form-control w-80" id="newUsersCheckPassword" required>
				<span id="usersCheckPasswordMessage"></span>
			</div>
			<button type="submit" class="btn custom-btn d-block mx-auto">비밀번호 재설정</button>
		</form>
	</div>
	<script>
	// 비밀번호 복잡도 체크
	document.getElementById("newUsersPassword").addEventListener("input", () => {
		const newUsersPassword = document.getElementById("newUsersPassword").value;
		const passwordMessage = document.getElementById("usersPasswordMessage");

		const isValidPassword = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,}/.test(newUsersPassword);

		if (newUsersPassword === "") {
			passwordMessage.textContent = "";
		} else if (!isValidPassword) {
			passwordMessage.textContent = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함한 10자 이상이어야 합니다.";
			passwordMessage.style.color = "red";
		} else {
			passwordMessage.textContent = "사용 가능한 비밀번호입니다.";
			passwordMessage.style.color = "green";
		}
	});

	// 비밀번호 일치 확인
	document.getElementById("newUsersPassword").addEventListener("input", pwdEqualValidate);
	document.getElementById("newUsersCheckPassword").addEventListener("input", pwdEqualValidate);

	function pwdEqualValidate() {
		const newUsersPassword = document.getElementById("newUsersPassword").value;
		const newUsersCheckPassword = document.getElementById("newUsersCheckPassword").value;
		const message = document.getElementById("usersCheckPasswordMessage");

		if (!newUsersPassword && !newUsersCheckPassword) {
			message.textContent = "";
			return;
		}

		if (newUsersPassword === newUsersCheckPassword) {
			message.textContent = "비밀번호가 일치합니다.";
			message.style.color = "green";
		} else {
			message.textContent = "비밀번호가 일치하지 않습니다.";
			message.style.color = "red";
		}
		}
	</script>

	
</body>
</html>