<%@page import="java.util.List"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.page.StayDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%
	String usersNum=(String)session.getAttribute("usersId");
	
	// 유저 로그인 여부 체크
	String usersId = (String)session.getAttribute("usersId");
	if (usersId == null) {
%>
	<script>
		alert("로그인이 필요합니다.");
		location.href="${pageContext.request.contextPath}/user/login-form.jsp";
	</script>
<%
		return;
	}
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>/stay/stay-new-form</title>
  
  <!-- 리소스 -->
  <jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>

  <!-- Toast UI Editor CSS/JS -->
  <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
  <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
  <script src="https://uicdn.toast.com/editor/latest/i18n/ko-kr.js"></script>

</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>

	<div class="container mt-4">
		<h1>숙소 등록</h1>
		<form action="stay-save.jsp" method="post" id="saveForm">
			<input type="hidden" name="users_num" value="관리자번호" />
			<label>숙소 이름: <input type="text" name="stay_name" required /></label><br>
				
			<label>숙소 지역:
				<select name="stay_loc" required>
					<option value="">-- 지역 선택 --</option>
					<option value="서울">서울</option>
					<option value="부산">부산</option>
					<option value="대구">대구</option>
					<option value="인천">인천</option>
					<option value="광주">광주</option>
					<option value="대전">대전</option>
					<option value="울산">울산</option>
					<option value="세종">세종</option>
					<option value="제주">제주</option>
					<option value="강원">강원</option>
					<option value="경기">경기</option>
					<option value="전북">전북</option>
					<option value="전남">전남</option>
					<option value="경북">경북</option>
					<option value="경남">경남</option>
					<option value="충북">충북</option>
					<option value="충남">충남</option>
				</select>
			</label><br>
	
	    	<label>
				숙소 주소: <input type="text" name="stay_addr" id="stay_addr" />
				<button type="button" id="addr">주소 확인</button>
	    	</label><br>
	
	    	<input type="hidden" name="stay_lat" id="stay_lat" />
	    	<input type="hidden" name="stay_long" id="stay_long" />
	
	    	<label>대표 번호: <input type="text" name="stay_phone" /></label><br>
	
			<label><input type="checkbox" name="facilities" value="수영장"> 수영장</label>
			<label><input type="checkbox" name="facilities" value="와이파이"> 와이파이</label>
			<label><input type="checkbox" name="facilities" value="조식"> 조식</label><br>
	
	    	<button type="submit">저장</button>
  		</form>
	</div>
	
	<script>
		
	</script>
	<!-- Kakao Map SDK
    <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9b6864c72d30ce2e92d016819ab66440&autoload=false&libraries=services"></script>

	Kakao 객체는 SDK 로딩 이후에만 접근 가능
	<script>
		document.addEventListener("DOMContentLoaded", function () {
		    kakao.maps.load(function () {
		      document.getElementById("addr").addEventListener("click", function (e) {
		        e.preventDefault();
	
		        const address = document.getElementById("stay_addr").value.trim();
		        if (!address) {
		          alert("주소를 입력하세요.");
		          return;
		        }
	
		        const geocoder = new kakao.maps.services.Geocoder();
		        geocoder.addressSearch(address, function (result, status) {
		          if (status === kakao.maps.services.Status.OK) {
		            const lat = result[0].y;
		            const lng = result[0].x;
	
		            document.getElementById("stay_lat").value = lat;
		            document.getElementById("stay_long").value = lng;
	
		            alert(`위도: ${lat}\n경도: ${lng}`);
		          } else {
		            alert("주소 검색 실패");
		          }
		        });
		      });
		    });
		  });
	</script>
	 -->
</body>
</html>
