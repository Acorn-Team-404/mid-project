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
<div class="container mt-5 mb-5" style="max-width: 500px;">
	<form action="${pageContext.request.contextPath}/user/find-id.jsp" method="get">
	    <div class="form-floating mb-4">
		    <input type="text" name="usersName" class="form-control" id="usersName" placeholder="이름" required>
		    <label for="usersName">이름</label>
	    </div>
	
	    <div class="form-floating mb-4">
	        <input type="email" name="email" class="form-control" id="email" placeholder="이메일" required>
	        <label for="email">이메일</label>
	    </div>
	    <button type="submit" class="btn custom-btn w-100">조회하기</button>
	  </form>
</div>
	
</body>
</html>