<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Boolean verified = (Boolean) session.getAttribute("emailVerified");    
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>user/signup-form</title>
</head>
<body>
	<div class="container">
		<h1>회원가입</h1>
		<form action="${pageContext.request.contextPath}/signup.user" method="post" autocapitalize="off">
			
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
				<span id="usersCheckPasswordMessage"></span>
			</div>
			
			<div>
				<label for="email">이메일</label>
				<input type="email" name="email" id="email" required>
				<button type="button" onclick="sendEmailAuth()">인증 코드 보내기</button>
				
				<input type="text" name="inputCode" id="inputCode" placeholder="인증코드 입력.." required>
				<button type="button" onclick="verifycode()">인증 코드 확인</button>
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
	
	
	const contextPath = "<%= request.getContextPath() %>";
	
	
	//이메일 인증 눌렀는지..
	document.querySelector("form").addEventListener("submit", function(e) {
		const verified = <%= verified != null && verified ? "true" : "false" %>;
		if (!verified) {
			e.preventDefault();
			alert("이메일 인증을 먼저 완료해주세요.");
		}
	});
	
		
	//인증번호 보내기, 버튼 누르면
	function sendEmailAuth() {
	    var email = document.getElementById("email").value;
	    fetch(contextPath + "/EmailAuth.user", {
	        method: "POST",
	        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
	        body: "usersEmail=" + encodeURIComponent(email) // ✅ 서버에서 받는 파라미터 이름에 맞춰야 함!
	    }).then(resp => resp.text()).then(text => {
	        console.log("서버 응답:", text); // ✅ 서버가 "success"나 "fail"을 응답
	        alert("인증 코드가 발송되었습니다.");
	    });
	}

	
	//인증확인 눌렀을 때
	function verifyCode() {
	    var code = document.getElementById("inputCode").value;
	    fetch("/${pageContext.request.contextPath}/VerifyCode.user", {
	        method: "POST",
	        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
	        body: "inputCode=" + encodeURIComponent(code)
	    }).then(resp => resp.text()).then(text => {
	        if (text === "success") {
	            alert("인증 완료");
	        } else {
	            alert("인증 실패");
	        }
	    });
	}
	
	
	
	
	
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

		fetch(contextPath +"/checkId.user?usersId=" + encodeURIComponent(usersId))
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
		
		
		
		//중복확인을 해야만 회원가입 가능
	document.querySelector("form").addEventListener("submit", function(e) {
		if (document.getElementById("checkIdAction").value !== "on") {
			e.preventDefault();
			alert("아이디 중복확인을 해주세요.");
		}
	});	
		
	}
	</script>
</body>
</html>
