<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>user/signup-form</title>
</head>
<body>
	<div class="container">
		<h1>회원가입</h1>
		<form action="${pageContext.request.contextPath}/user/signup" method="post" autocapitalize="off">
			
			<div>
				<label for="usersName">성함</label>
				<input type="text" name="usersName" id="usersName" required>
			</div>
			
			<div>
  				<label for="usersId">아이디</label>
  				<input type="text" name="usersId" id="usersId" required>
				<input type="hidden" name="checkIdAction" id="checkIdAction" value="off">
				<button type="button" onclick="IdCheck()">중복확인</button>
				<span id="usersIdMessage"></span>
			</div>
			
			<div>
				<label for="usersPassword">비밀번호</label>
				<input type="password" name="usersPassword" id="usersPassword" required>
				<span id="usersPasswordMessage"></span>
			</div>
			
			<div>
				<label for="usersCheckPassword">비밀번호 확인</label>
				<input type="password" name="usersCheckPassword" id="usersCheckPassword" required>
				<span id="usersCheckPasswordMessage" style="font-size: 0.9em;"></span>
			</div>
			
			<div>
				<label for="email">이메일</label>
				<input type="email" name="email" id="email" required>
			</div>
			
			<div>
				<label for="phone">연락처</label>
				<input type="text" name="phone" id="phone" maxlength="13" oninput="Hyphen(this)" required>
			</div>
			
			<div>
				<label for="birth">생년월일</label>
				<input type="date" name="birth" id="birth" required>
			</div>
			
			<button type="submit">가입</button>
		</form>
	</div>
	
	<script>
	// 비밀번호 복잡도 체크
	document.getElementById("usersPassword").addEventListener("input", () => {
		const usersPassword = document.getElementById("usersPassword").value;
		const passwordMessage = document.getElementById("usersPasswordMessage");

		const isValidPassword = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,}/.test(usersPassword);

		if (usersPassword === "") {
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
	document.getElementById("usersPassword").addEventListener("input", pwdEqualValidate);
	document.getElementById("usersCheckPassword").addEventListener("input", pwdEqualValidate);

	function pwdEqualValidate() {
		const usersPassword = document.getElementById("usersPassword").value;
		const usersCheckPassword = document.getElementById("usersCheckPassword").value;
		const message = document.getElementById("usersCheckPasswordMessage");

		if (!usersPassword && !usersCheckPassword) {
			message.textContent = "";
			return;
		}

		if (usersPassword === usersCheckPassword) {
			message.textContent = "비밀번호가 일치합니다.";
			message.style.color = "green";
		} else {
			message.textContent = "비밀번호가 일치하지 않습니다.";
			message.style.color = "red";
		}
	}

	// 아이디 유효성 검사
	document.getElementById("usersId").addEventListener("input", () => {
		const usersId = document.getElementById("usersId").value;
		const idMessage = document.getElementById("usersIdMessage");
		const isValidId = /^[a-zA-Z0-9]{4,20}$/.test(usersId);

		if (usersId === "") {
			idMessage.textContent = "";
		} else if (!isValidId) {
			idMessage.textContent = "아이디는 영문+숫자 4~20자입니다.";
			idMessage.style.color = "red";
		} else {
			idMessage.textContent = "사용 가능한 아이디입니다.";
			idMessage.style.color = "green";
		}
		// 중복확인 초기화
		document.getElementById("checkIdAction").value = "off";
	});

	// 아이디 중복 확인
	function IdCheck() {
		const usersId = document.getElementById("usersId").value;
		const checkIdAction = document.getElementById("checkIdAction");

		fetch("${pageContext.request.contextPath}/user/check-id?usersId=" + encodeURIComponent(usersId))
			.then(response => response.text())
			.then(data => {
				if (data.trim() === "empty") {
					alert("아이디를 입력하세요.");
					checkIdAction.value = "off";
				} else if (data.trim() === "exist") {
					alert("중복된 아이디입니다.");
					checkIdAction.value = "off";
				} else {
					alert("사용 가능한 아이디입니다.");
					checkIdAction.value = "on";
				}
			})
			.catch(error => {
				console.error("중복 확인 에러:", error);
			});
	}

	// 연락처 하이픈 자동 삽입
	function Hyphen(target) {
		target.value = target.value
			.replace(/[^0-9]/g, '')
			.replace(/^(\d{3})(\d{4})(\d{0,4})$/, '$1-$2-$3')
			.replace(/(-{1,2})$/, '');
	}
	</script>
</body>
</html>
