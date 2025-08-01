<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>마이페이지</title>
  <jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
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
  <div class="row">
  
 
    <div class="col-md-3">
      <div class="list-group">
     	<!-- 회원 정보 관련 바로가기 -->
        <a href="${pageContext.request.contextPath}/my-page" class="list-group-item list-group-item-action active">회원 정보 수정</a>
        <a href="${pageContext.request.contextPath}/booking/confirm" class="list-group-item list-group-item-action">예약 내역</a> <!-- 임시로 booking/confirm 보내는 중 -->
        <a href="${pageContext.request.contextPath}/*.post" class="list-group-item list-group-item-action">내 게시글</a> <!-- 임시로 *.post 보내는 중 -->
        <a href="${pageContext.request.contextPath}/my-page" class="list-group-item list-group-item-action">내 댓글</a> <!-- 임시로 마이페이지 보내는 중 -->
        <a href="${pageContext.request.contextPath}/inquiry/list.jsp" class="list-group-item list-group-item-action">문의 내역</a> <!-- 임시로 inquiry/list.jsp 보내는 중 -->
        <a href="${pageContext.request.contextPath}/logout" onClick="confirmLogout(event)" class="list-group-item list-group-item-action text-danger">로그아웃</a>
      </div>
    </div>
    
    <div class="col-md-9">
      <div class="card p-4 shadow-sm">
        <div class="row">
          <!-- 회원 정보 -->
          <div class="col-md-8">
            <table class="table table-borderless">
              <tr>
                <th style="width: 30%;">이메일</th>
                <td style="color:gray; ">${user.usersEmail}</td>
              </tr>
              <tr>
                <th>이름</th>
                <td >
                  ${user.usersName}
                  <a href="${pageContext.request.contextPath}/my-page/update-name.jsp" class="btn btn-outline-secondary btn-sm ms-2">변경하기</a>
                </td>
              </tr>
              <tr>
                <th>전화번호</th>
                <td>
                  ${formattedPhone} <!-- 이렇게 가져와야 010-0000-0000 형태로 나와서 우선 해보는 중 -->
                  <a href="${pageContext.request.contextPath}/my-page/update-phone.jsp" class="btn btn-outline-secondary btn-sm ms-2">변경하기</a>
                </td>
              </tr>
              <tr>
                <th>생년월일</th>
                <td>${user.usersBirth}</td>
              </tr>
            </table>
          </div>

          <!-- 프로필 이미지 -->
          <div class="col-md-4 text-center">
            <img src="${pageContext.request.contextPath}/images/${user.usersProfileImage}" alt="프로필 이미지" class="profile-img mb-3" />
            <form action="${pageContext.request.contextPath}/update-profile-image" method="post" enctype="multipart/form-data">
              <input type="file" name="profileImage" accept="image/*" class="form-control form-control-sm mb-2" />
              <button type="submit" class="btn btn-sm btn-outline-primary">업로드</button>
            </form>
          </div>
        </div>
      </div>
    </div>
    
  </div>
</div>
	<script>
		function confirmLogout(e) {
			e.preventDefault();
			// 만약 로그아웃을 하겠냐고 물어보고
			if(confirm("로그아웃 하시겠습니까?")) {
				// 확인을 누르면 logoutServlet으로 이동
				 location.href = "<%=request.getContextPath()%>/logout";
			}
		}
	</script>
</body>
</html>