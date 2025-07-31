<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr"
	crossorigin="anonymous">
<style>
  .profile-img {
    width: 120px;
    height: 120px;
    object-fit: cover;
    border-radius: 50%;
    border: 2px solid #ddd;
  }
</style>
</head>
<body>

<div class="container my-5">

	<h2 class="mb-4">마이페이지</h2>

	<div class="card p-4 shadow-sm">
		<div class="row align-items-center">
			<!-- 회원 정보 왼쪽 -->
			<div class="col-md-9">
				<table class="table table-borderless">
					<tr>
						<th class="text-end" style="width: 25%;">이메일</th>
						<td style="color:grey;">${users.email}</td>
					</tr>
					<tr>
						<th class="text-end">이름</th>
						<td>${users_name }
						<button type="button" class="btn btn-outline-secondary btn-sm ms-2">변경하기</button>
						</td>					
					</tr>
					<tr>
						<th class="text-end">전화번호</th>
						<td >${users_phone}
						<button type="button" class="btn btn-outline-secondary btn-sm ms-2">변경하기</button>
						</td>
					</tr>
					<tr>
						<th class="text-end">생년월일</th>
						<td>${users_birth}</td>
					</tr>
				</table>
			</div>
			<div class="col-md-3 text-center">
				<img src="${pageContext.request.contextPath}/images/${users_profile_image}" alt="프로필 이미지" class="profile-img"><br />
			</div>
		</div>
	</div>

</div>
</body>
</html>
