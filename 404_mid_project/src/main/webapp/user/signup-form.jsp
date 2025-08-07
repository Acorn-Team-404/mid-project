<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  
  <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
  <title>user/signup-form</title>
  <style>
    .custom-btn {
      background-color: #d5d5d5;
      /* 초록 */
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

    #birth {
      size: 100rem;
    }
    
  </style>
</head>

<body>

  <h1 class="text-center mt-4">J O I N</h1>
  <h2 class="text-center mt-4">회원가입</h2>

  <div class="container mt-5 mb-5" style="max-width: 500px;">
  	<form action="${pageContext.request.contextPath}/user/signup.jsp" method="post" autocapitalize="off" onsubmit="return checkForm()">

      <div class="form-floating mb-4">
          <input class="form-control w-80" type="text" name="usersName" id="usersName" placeholder="이름" required>
          <label for="usersName" class="form-label">이름</label>
      </div>

      <div class="mb-4">
		  <div class="input-group">
		      <div class="form-floating flex-grow-1">
		          <input class="form-control" type="text" name="usersId" id="usersId" placeholder="아이디" required>
		          <label for="usersId">아이디</label>
		      </div>
		      <input type="hidden" id="checkIdAction" name="checkIdAction" value="off">
		      <button type="button" onclick="IdCheck()" class="btn custom-btn">중복확인</button>
		  </div>
		  <span id="usersIdMessage" class="ms-1"></span>
	  </div>

      <div class="form-floating mb-4">
		  <input class="form-control" type="password" name="usersPassword" id="usersPassword" placeholder="비밀번호" required>
		  <label for="usersPassword">비밀번호</label>
	      <span id="usersPasswordMessage" class="ms-1"></span>
	  </div>


      <div class="form-floating mb-4">
	  	  <input class="form-control" type="password" name="usersCheckPassword" id="usersCheckPassword" placeholder="비밀번호 확인" required>
		  <label for="usersCheckPassword">비밀번호 확인</label>
		  <span id="usersCheckPasswordMessage" class="ms-1"></span>
	  </div>

	<div class="mb-2">
		<div class="input-group">
			<div class="form-floating flex-grow-1">
			    <input class="form-control" type="email" name="email" id="email" placeholder="이메일" required>
			    <label for="email">이메일</label>
			</div>
		<button type="button" onclick="EmailCheck()" class="btn custom-btn">중복확인</button>
		</div>	
		<button type="button" onclick="sendEmailAuth()" class="btn custom-btn w-100 mt-2" id="send-code">인증 코드 전송</button>
		<span id="EmailCheckMessage" class="ms-1"></span>
	</div>	
		
		  <!-- 인증 코드 보내기 버튼 -->
	
		
		  <!-- 인증 코드 입력창 -->
	<div id="code-box" class="input-group mb-4" style="display: none;">
		  <div class="form-floating flex-grow-1">
			    <input class="form-control" type="text" name="inputCode" id="inputCode" placeholder="인증코드 입력">
			    <label for="inputCode">인증코드 입력</label>
		  </div>
		  <button type="button" onclick="verifyCode()" class="btn custom-btn">확인</button>
		  <button type="button" onclick="sendEmailAuth()" class="btn custom-btn" id="resend-code" style="display: none;">재전송</button>
	</div>

		
		<input type="hidden" id="emailVerified" name="emailVerified" value="false">
		<input type="hidden" id="emailVerified2" name="emailVerified2" value="false">
	


     <div class="form-floating mt-4 mb-4">
	 	<input class="form-control" type="text" name="phone" id="phone" maxlength="13" oninput="Hyphen(this)" placeholder="연락처" required>
		<label for="phone">연락처</label>
	 </div>

	<div class="form-floating mb-5">
		  <input class="form-control" type="date" name="birth" id="birth" placeholder="생년월일" required>
		  <label for="birth">생년월일</label>
	</div>

      <button type="submit" class="btn custom-btn d-block mx-auto">가입</button>
    </form>
  </div>
  <script>


	  
    const contextPath = "<%= request.getContextPath() %>";
	var emailVerified = false;
	//document.querySelector("#code-box").style.display = "flex";
	
	

	//이메일이 비었을 때 입력하라는 창 
	const inputEmail = document.getElementById("email").value
	
	if(inputEmail ==null){
		
	}
	 
	
	//이메일 중복 인증하기
    function EmailCheck() {
    	  const usersEmail = document.getElementById("email").value; //
    	  const checkEmailAction = document.getElementById("emailVerified2");
    	  
    	  if(!usersEmail){
    		  showInfoModal("이메일을 입력하세요");
    		  checkEmailAction.value = "false";
    		  return;
    	  }
   
    	fetch(contextPath + "/user/check-email.jsp?usersEmail=" + encodeURIComponent(usersEmail))
    	  .then(response => response.text())
    	  .then(data => {
    	      if (data.trim() === "exist") {
    	      showInfoModal("중복된 이메일입니다."); 	
    	      //alert("중복된 이메일입니다.");
    	      checkEmailAction.value = "false";
    	    } else {
    	       showInfoModal("사용 가능한 이메일 입니다");	
    	      //alert("사용 가능한 이메일입니다.");
    	      checkEmailAction.value = "true"; //  true여야 form에서 통과
    	    }
    	  });
    }	
	
	
	
	
	
	//인증번호 확인, 아이디중복확인을 안눌렀을 때
	function checkForm(){
		const checkId = document.getElementById("checkIdAction").value;
		const checkEmail = document.getElementById("emailVerified").value;

		
		console.log("checkIdAction:", checkId);
		console.log("emailVerified:", checkEmail);
		
		if(checkId !== "on"){
			showInfoModal("아이디 중복확인을 해주세요.");
			//alert("아이디 중복확인을 주세요");
			return false;
		}else if(checkEmail  !== "true"){
			showInfoModal("이메일 인증을 완료해주세요");
			//alert("이메일 인증을 완료해주세요");
			return false;
		}else return true; //둘다 눌렀을 떄...
	}
	
	
	
    //인증번호 보내기, 버튼 누르면
	function sendEmailAuth() {
	  const email = document.getElementById("email").value;
	  
	  if (email === "") {
		  	showInfoModal("이메일을 입력하세요");
		    //alert("이메일을 입력하세요.");
		    document.getElementById("email").focus();
		    return;
		  }else{
			  document.getElementById("code-box").style.display = "flex";
			  document.getElementById("send-code").style.display = "none"; 
			  document.getElementById("resend-code").style.display = "inline-block";
		  }
	  
	  fetch(contextPath + "/user/email-auth.jsp", {
	    method: "POST",
	    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	    body: "usersEmail=" + encodeURIComponent(email)
	  }).then(resp => resp.text()).then(text => {
	    //alert("인증 코드가 발송되었습니다.");

	  });
	}

	function checkEmailVerification() {
		  const verified = document.getElementById("emailVerified").value;
		  if (verified !== "true") {
			showInfoModal("이메일 인증을 완료해 주세요");  
		    //alert("이메일 인증을 완료해주세요.");
		    return false;
		  }
	  	return true;
	}
	
    //인증확인 눌렀을 때
	function verifyCode() {
	  var code = document.getElementById("inputCode").value;
	
	  fetch(contextPath + "/user/verify-code.jsp", {
	    method: "POST",
	    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	    body: "inputCode=" + encodeURIComponent(code)
	  })
	    .then(resp => resp.text())
	    .then(text => {
	
	      if (text.trim() === "success") {
	    	showInfoModal("이메일 인증 성공!");  
	        //alert("이메일 인증 성공!");
	        emailVerified = true;
	        document.getElementById("emailVerified").value = "true";
	        
	        document.getElementById("code-box").style.display = "none";
	        document.getElementById("resend-code").style.display = "none";

	        
	        EmailCheckMessage.textContent = "이메일 인증에 성공했습니다";
	        EmailCheckMessage.style.color = "#c1c2c1";
	        
	        
	        
	      } else if (text.trim() === "expired") {
	    	showInfoModal("인증번호가 만료되었습니다");
	        //alert("인증번호가 만료되었습니다.");
	        emailVerified = false;
	        document.getElementById("emailVerified").value = "false";
	      } else {
	    	showInfoModal("인증번호가 틀렸습니다");
	    	//alert("인증번호가 틀렸습니다.");
	        emailVerified = false;
	        document.getElementById("emailVerified").value = "false";
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
        passwordMessage.textContent = "  비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함한 10자 이상";
        passwordMessage.style.color = "red";
      } else {
        passwordMessage.textContent = "  사용 가능한 비밀번호입니다.";
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
        message.textContent = "  비밀번호가 일치합니다.";
        message.style.color = "green";
      } else {
        message.textContent = "  비밀번호가 일치하지 않습니다.";
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
        idMessage.textContent = "  아이디는 영문+숫자 4~20자입니다.";
        idMessage.style.color = "red";
      } else {
        idMessage.textContent = "  사용 가능한 아이디입니다.";
        idMessage.style.color = "green";
      }
      // 중복확인 초기화
      document.getElementById("checkIdAction").value = "off";
    });

    // 아이디 중복 확인
    function IdCheck() {
      const usersId = document.getElementById("usersId").value;
      const checkIdAction = document.getElementById("checkIdAction");

      fetch(contextPath + "/user/check-id.jsp?usersId=" + encodeURIComponent(usersId))
        .then(response => response.text())
        .then(data => {
          if (data.trim() === "empty") {
        	  
        	showInfoModal("아이디를 입력하세요.");
            //alert("아이디를 입력하세요.");
            checkIdAction.value = "off";
          } else if (data.trim() === "exist") {
        	showInfoModal("중복된 아이디 입니다.");
            checkIdAction.value = "off";
          } else {
        	showInfoModal("사용가능한 아이디 입니다.");
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
    

    // 스크립트 마지막에 추가
    flatpickr("#birth", {
      dateFormat: "Y-m-d",
      wrap: false,
      defaultDate: "today",
      disableMobile: true // 모바일에서도 팝업 보이게
    });

    function showInfoModal(message) {
    	  const modalMessage = document.getElementById("modalMessage");
    	  modalMessage.textContent = message;

    	  const infoModal = new bootstrap.Modal(document.getElementById('infoModal'));
    	  infoModal.show();
    	}
  </script>
  <jsp:include page="/WEB-INF/include/user-modal.jsp" />
 
</body>

</html>