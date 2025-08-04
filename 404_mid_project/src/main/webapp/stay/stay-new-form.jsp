<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/stay/new-form</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>

<!-- Toast UI Editor CSS -->
<link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
<!-- Toast UI Editor JS -->
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<!-- 한국어 번역 파일 추가 -->
<script src="https://uicdn.toast.com/editor/latest/i18n/ko-kr.js"></script>
</head>
<body>
	<form action="stay-save.jsp" method="post">
	    <label>숙소 이름: <input type="text" name="stay_name" /></label><br>
	    <label>숙소 주소: <input type="text" name="stay_addr" id="stay_addr" /></label><br>
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
	    <input type="hidden" name="stay_lat" id="stay_lat" />
	    <input type="hidden" name="stay_long" id="stay_long" />
	    <label>대표 번호: <input type="text" name="stay_phone" /></label><br>
	    <label><input type="checkbox" name="facilities[]" value="수영장"> 수영장</label>
		<label><input type="checkbox" name="facilities[]" value="와이파이"> 와이파이</label>
		<label><input type="checkbox" name="facilities[]" value="조식"> 조식</label>
	    <button type="button" onclick="getCoordinates()">좌표 가져오기</button>
	    <button type="submit">등록</button>
	</form>
	<!-- Kakao Map JS -->
	<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9b6864c72d30ce2e92d016819ab66440&libraries=services"></script>
	<script>
		function getCoordinates() {
		    const address = document.getElementById("stay_addr").value;
		    const geocoder = new kakao.maps.services.Geocoder();
	
			geocoder.addressSearch(address, function(result, status) {
				if (status === kakao.maps.services.Status.OK) {
		            const lat = result[0].y;
		            const lng = result[0].x;
		            document.getElementById("stay_lat").value = lat;
		            document.getElementById("stay_long").value = lng;
		            alert("좌표: " + lat + ", " + lng);
				} else {
					alert("실패");
				}
			});
		}
	</script>
</body>
</html>