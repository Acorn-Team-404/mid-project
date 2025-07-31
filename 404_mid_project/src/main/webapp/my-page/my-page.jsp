<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<div class="row align-items-start">
			<!-- 왼쪽 회원 정보 -->
			<div class="col-md-9">
				<table class="table table-borderless">
					<tr>
						<th class="text-end" style="width: 25%;">이메일</th>
						<td style="color:grey;">${user.usersEmail}</td>
					</tr>
					<tr>
  						<th class="text-end">이름</th>
  						<td>
    						${user.usersName}
    						<a href="${pageContext.request.contextPath}/my-page/update-name.jsp" class="btn btn-outline-secondary btn-sm ms-2">변경하기</a>
  						</td>
					</tr>
					<tr>
						<th class="text-end">전화번호</th>
						<td>${user.usersPhone}
							 <a href="${pageContext.request.contextPath}/my-page/update-phone.jsp" class="btn btn-outline-secondary btn-sm ms-2">변경하기</a>
						</td>
					</tr>
					<tr>
						<th class="text-end">생년월일</th>
						<td>${user.usersBirth}</td>
					</tr>
				</table>
			</div>

			<!-- 오른쪽 프로필 이미지 및 업로드 폼 -->
			<div class="col-md-3 text-center">
				<img src="${pageContext.request.contextPath}/images/${user.usersProfileImage}" alt="프로필 이미지" class="profile-img mb-3" />
				
				<form action="${pageContext.request.contextPath}/update-profile-image" method="post" enctype="multipart/form-data">
					<div class="mb-2">
						<input type="file" name="profileImage" accept="image/*" class="form-control form-control-sm" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

</body>
</html>