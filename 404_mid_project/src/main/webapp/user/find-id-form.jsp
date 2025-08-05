<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<title>user/findid.form</title>
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

  </style>
</head>
<body>
	<h1 class="text-center mt-4">아이디 찾기</h1>
	<div class="container mt-5 mb-5">
		<form action="${pageContext.request.contextPath}/user/find-id.jsp" method="get">
			<div class="mb-4">
				<label for="usersName" class="form-label">이름</label>
				<input type="text" name="usersName" class="form-control w-80"  id="usersName"/>
			</div>
			<div class="mb-4">
				<label for="email" class="form-label">이메일</label>
				<input type="text" name="email" class="form-control w-80"  id="email"/>
			</div>
			<button type="submit" class="btn custom-btn" >조회하기</button>
		</form>
	</div>
	
</body>
</html>