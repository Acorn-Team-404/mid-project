<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/test/link.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<h1>각종 링크 연결 페이지 (세션에 data 실어갈 수 있음)</h1>
	<br />
	<h3>이미지 업로드 링크</h2>
	<ul>
		<li>
			<form action="${pageContext.request.contextPath}/admin/image-upload.jsp">
				<button type="submit" class="btn btn-primary">index 파일 업로드</button>
				<input type="hidden" name="imageAction" value="index"/>
			</form>
		</li>
		<li>
			<form action="${pageContext.request.contextPath}/admin/image-upload.jsp">
				<button type="submit" class="btn btn-primary">room 파일 업로드</button>
				<input type="hidden" name="imageAction" value="room"/>
			</form>
		</li>
		<li>
			<form action="${pageContext.request.contextPath}/admin/image-upload.jsp">
				<button type="submit" class="btn btn-primary">stay 파일 업로드</button>
				<input type="hidden" name="imageAction" value="stay"/>
			</form>
		</li>
	</ul>
	<br />
	<h3>객실 예약페이지</h3>
	<ul>
		<li>
			<form action="${pageContext.request.contextPath}/booking/booking-page.jsp">
				<button type="submit" class="btn btn-primary">객실 예약</button>
				<input type="hidden" name="" value=""/>
			</form>
		</li>
		<li>
			<form action="${pageContext.request.contextPath}/booking/booking-page2.jsp">
				<button type="submit" class="btn btn-primary">객실 예약2</button>
				<input type="hidden" name="" value=""/>
			</form>
		</li>
	</ul>
</body>
</html>