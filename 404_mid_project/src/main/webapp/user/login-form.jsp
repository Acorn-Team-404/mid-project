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
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
	<title>/user/login-form.jsp</title>
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
	    .text-center a {
	      color: gray !important;
	      text-decoration: none;
  		}
  		.text-center a:hover{
	      color: darkgray !important;
	      text-decoration: none;
  		}
 	</style>
</head>
<body>
	<h1 class="text-center mt-4">로그인</h1>
	<div class="container mt-5 mb-5">
		<form action="${pageContext.request.contextPath}/user/login.jsp" method="post">
			<%-- 로그인 성공후에 이동할 url 정보를 추가로 form 전송되도록 한다 --%>
			<input type="hidden" name="url" value="<%=url %>"/>
			<div class="mb-4">
				<label for="usersId" class="form-label">아이디</label>
				<input type="text" name="usersId" class="form-control w-80" id="usersId"/>
			</div>
			<div class="mb-4">
				<label for="usersPassword" class="form-label">비밀번호</label>
				<input type="password" name="usersPassword" class="form-control w-80"  id="usersPassword"/>
			</div>
			<button type="submit" class="btn custom-btn d-block mx-auto">로그인</button><br>
			<div class="text-center" style="color: gray;">
				<a href="${pageContext.request.contextPath}/user/find-id-form.jsp">아아디 찾기</a>
				<span> | </span>
				<a href="${pageContext.request.contextPath}/user/find-password-form.jsp">비밀번호 찾기</a>
			</div>
		</form>
	</div>

</body>
</html>