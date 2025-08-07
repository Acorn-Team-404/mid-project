<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String url = request.getParameter("url");
	if (url == null) {
		String cPath = request.getContextPath();
		url = cPath + "/index.jsp";
	}
	
	//쿠키에 저장된 아이디와 비밀번호를 담을 변수
	String savedUsersId="";
	// HttpServletRequest 객체의 메소드를 이용해서 전달된 쿠키 목록을 얻어낼수 있다
	Cookie[] cooks=request.getCookies();
	if(cooks!=null){
		//반복문 돌면서 쿠키객체를 하나씩 참조해서 
		for(Cookie tmp: cooks){
			//저장된 키값을 읽어온다.
			String key=tmp.getName();
			//만일 키값이 savedUserName 라면 
			if(key.equals("savedUserId")){
				//쿠키 value 값을 savedUserName 라는 지역변수에 저장
				savedUsersId=tmp.getValue();
			}
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>로그인</title>
	<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
  <style>
    html, body {
      height: 100%;
      margin: 0;
      background-color: #f8f9fa;
    }

    .wrapper {
      height: calc(100vh - 70px); /* 네비바 높이를 뺀 나머지 영역 */
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .login-box {
      width: 100%;
      max-width: 400px;
      padding: 30px;
      background-color: white;
      border-radius: 10px;
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
    }

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

    .text-center a {
      color: gray !important;
      text-decoration: none;
    }

    .text-center a:hover {
      color: darkgray !important;
    }
    
    .form-check-input:checked {
	  background-color: #c1c2c1;
	  border-color: #808080;  
	}

	.form-check-input:focus {
		box-shadow: 0 0 0 0 rgba(0, 0, 0, 0);
		border: 0.5px solid #808080; /* 원하는 테두리 색 */
	}
	.form-check-input {
	  border: 0.5px solid #808080; /* 원하는 테두리 색 */
	  background-color: white;
	}
	.form-check-input {
	  border: 0.1px solid #808080; /* 원하는 테두리 색 */
	  background-color: white;
	}
    
  </style>
</head>
<body>
  <!-- 네비바는 여기 바디 안에 유지 -->
  <jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>

  <!-- wrapper로 로그인 박스를 감싸서 정중앙 정렬 -->
  <div class="wrapper">
    <div class="login-box">
      <h2 class="text-center mb-4">로그인</h2>
      <form action="${pageContext.request.contextPath}/user/login.jsp" method="post">
        <input type="hidden" name="url" value="<%=url %>"/>

        <div class="form-floating mb-4">
          <input value="<%=savedUsersId %>" type="text" name="usersId" class="form-control" id="usersId" placeholder="아이디" required>
          <label for="usersId">아이디</label>
        </div>


        <div class="form-floating mb-4">
          <input type="password" name="usersPassword" class="form-control" id="usersPassword" placeholder="비밀번호" required>
          <label for="usersPassword">비밀번호</label>
        </div>

		<div class="form-check text-start my-3">
			<input class="form-check-input" type="checkbox" name="isSave" value="yes" id="flexCheckDefault"
         	<%=savedUsersId.equals("") ? "" : "checked" %>>
 			 <label class="form-check-label" for="flexCheckDefault">아이디 정보 저장</label>
		</div>

        <button type="submit" class="btn custom-btn w-100">로그인</button><br><br>

        <div class="text-center">
          <a href="${pageContext.request.contextPath}/user/find-id-form.jsp">아이디 찾기</a>
          <span> | </span>
          <a href="${pageContext.request.contextPath}/user/find-password-form.jsp">비밀번호 찾기</a>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
