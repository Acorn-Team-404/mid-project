<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="model.user.UserDao"%>
<%@page import="model.user.UserDto"%>
<%@page import="model.pay.PaymentDao"%>
<%@page import="model.pay.PaymentDto"%>
<%@page import="model.page.StayDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.room.RoomDao"%>
<%@page import="model.book.BookDao"%>
<%@page import="model.book.BookDto"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
// 1. GET 파라미터로 bookNum 받기
String bookNum = request.getParameter("bookNum");
if (bookNum == null || bookNum.trim().isEmpty()) {
	// bookNum 없으면 예약 페이지로 리다이렉트
	response.sendRedirect(request.getContextPath() + "/booking/booking-page.jsp");
	return;
}

// 2. DB에서 예약 정보 가져오기
BookDto bookDto = BookDao.getInstance().getByBookNum(bookNum);
if (bookDto == null) {
	// 예약 정보 없으면 예약 페이지로 리다이렉트
	response.sendRedirect(request.getContextPath() + "/booking/booking-page.jsp");
	return;
}

// 3. 관련 정보 조회
StayDto stayDto = StayDao.getInstance().getByBookStayNum(bookDto.getBookStayNum());
RoomDto roomDto = RoomDao.getInstance().getByNum(bookDto.getBookRoomNum());
UserDto userDto = UserDao.getInstance().getByUserNum(bookDto.getBookUsersNum());

// 4. 결제 토큰은 세션이나 다른 방식으로 받는 게 좋음. 예시로 세션에서 받기:
session = request.getSession(false);
String payToken = null;
if (session != null) {
	payToken = (String) session.getAttribute("payToken");
}
if (payToken == null) {
	// 토큰 없으면 결제 페이지 진입 불가
	response.sendRedirect(request.getContextPath() + "/booking/booking-page.jsp");
	return;
}

// 5. 날짜 포맷팅
SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

String checkInFormat = "";
String checkOutFormat = "";

try {
	Date checkInDate = inputFormat.parse(bookDto.getBookCheckIn());
	Date checkOutDate = inputFormat.parse(bookDto.getBookCheckOut());

	checkInFormat = outputFormat.format(checkInDate);
	checkOutFormat = outputFormat.format(checkOutDate);
} catch (java.text.ParseException e) {
	e.printStackTrace();
}

// 6. 필요 시 bookNum 세션에 저장 (선택)
if (session != null) {
	session.setAttribute("bookNum", bookDto.getBookNum());
}
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>결제 페이지</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
<script>
	window.contextPath = "${pageContext.request.contextPath}";
</script>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script src="https://js.tosspayments.com/v2/standard"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/payments/payments.css">
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>

	<div class="container">
		<div class="card">
			<h2 class="mb-4 text-center">예약 확인 및 결제</h2>
			<div class="section section-box">
				<h2 class="h5">숙소 정보</h2>
				<p>
					<strong>숙소명:</strong>
					<%=stayDto.getStayName()%></p>
				<p>
					<strong>위치:</strong>
					<%=stayDto.getStayAddr()%></p>
				<p>
					<strong>전화번호:</strong>
					<%=stayDto.getStayPhone()%></p>
			</div>

			<div class="section section-box">
				<h2 class="h5">예약 상세</h2>
				<p>
					<strong>예약 번호:</strong>
					<%=bookDto.getBookNum()%></p>
				<div class="row row-cols-2">
					<div>
						<h3 class="h6">체크인</h3>
						<p>
							날짜:
							<%=checkInFormat%></p>
						<p>
							시간:
							<%=bookDto.getBookCheckInTime()%></p>
					</div>
					<div>
						<h3 class="h6">체크아웃</h3>
						<p>
							날짜:
							<%=checkOutFormat%></p>
						<p>시간: AM 11 : 00</p>
					</div>
				</div>
			</div>

			<div class="section section-box">
				<h2 class="h5">투숙객 정보</h2>
				<p>
					<strong>예약자 성함:</strong>
					<%=userDto.getUsersName()%></p>
				<p>
					<strong>투숙 인원:</strong>
					<%=bookDto.getBookTotalPax()%></p>
				<ul>
					<li>성인: <%=bookDto.getBookAdult()%></li>
					<li>어린이: <%=bookDto.getBookChildren()%></li>
					<li>유아: <%=bookDto.getBookInfant()%></li>
				</ul>
				<p>
					<strong>객실 타입:</strong>
					<%=roomDto.getRoomType()%></p>
				<p>
					<strong>요청 사항: </strong>
					<%=bookDto.getBookRequest()%></p>
			</div>

			<div class="section section-box">
				<h2 class="h5">결제 정보</h2>
				<p>
					<strong>총 결제 금액:</strong>
					<%=bookDto.getBookTotalAmount()%></p>
			</div>

			<div class="section section-box">
				<h2 class="h5">취소 및 이용 안내</h2>
				<p>
					<strong>취소 규정:</strong> 체크인 10일 전까지: 총 결제금액의 100% 환불 <br /> 체크인 9일
					전까지: 총 결제금액의 90% 환불 <br /> 체크인 8일 전까지: 총 결제금액의 80% 환불 <br /> 체크인
					7일 전까지: 총 결제금액의 70% 환불 <br /> 체크인 6일 전까지: 총 결제금액의 60% 환불 <br />
					체크인 5일 전까지: 총 결제금액의 50% 환불 <br /> 체크인 4일 전까지: 총 결제금액의 40% 환불 <br />
					체크인 3일 전부터: 변경 및 환불 불가
				</p>
				<br />
				<p>
					<strong>이용 약관:</strong> 체크인은 오후 4시, 체크아웃은 오전 11시입니다. 최대 인원을 초과할 경우
					입실이 제한되며, 외부 방문객은 제한됩니다.
				</p>
			</div>

			<div class="section section-box">
				<h2 class="h5 mb-3">결제 수단 선택</h2>
				<div id="payment-method">
					<button id="CARD" type="button" class="button2"
						onclick="selectPaymentMethod('CARD')">카드 및 간편 결제</button>
					<button id="TRANSFER" type="button" class="button2"
						onclick="selectPaymentMethod('TRANSFER')">계좌이체</button>
					<button id="MOBILE_PHONE" type="button" class="button2"
						onclick="selectPaymentMethod('MOBILE_PHONE')">휴대폰 결제</button>

				</div>
				<button type="button" class="button mt-4" onclick="requestPayment()">결제하기</button>
			</div>
		</div>
	</div>

	<form action="${pageContext.request.contextPath}/pay/PaymentsServlet"
		method="post">
		<input type="hidden" name="bookNum" value="<%=bookDto.getBookNum()%>">
		<input type="hidden" name="bookStayNum"
			value="<%=bookDto.getBookStayNum()%>" /> <input type="hidden"
			name="bookRoomNum" value="<%=bookDto.getBookRoomNum()%>" /> <input
			type="hidden" name="amount" value="<%=bookDto.getBookTotalAmount()%>" />
		<input type="hidden" name="payToken" value="${payToken}" />
	</form>

	<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>

	<script>
  // 전역 scope에 예약 정보 저장
  window.bookingInfo = {
    bookNum: "<%=bookDto.getBookNum()%>",
    stayNum: "<%=bookDto.getBookStayNum()%>",
    roomNum: "<%=bookDto.getBookRoomNum()%>",
    roomName: "<%=roomDto.getRoomName()%>",
    amount: "<%=bookDto.getBookTotalAmount()%>",
    totalPax: "<%=bookDto.getBookTotalPax()%>",
    checkInDate: "<%=bookDto.getBookCheckIn()%>",
    checkOutDate: "<%=bookDto.getBookCheckOut()%>",
    userName: "<%=userDto.getUsersName()%>",
    roomType: "<%=roomDto.getRoomType()%>",
    stayName: "<%=stayDto.getStayName()%>",
    stayAddr: "<%=stayDto.getStayAddr()%>
		"
		};
	</script>
	<script
		src="${pageContext.request.contextPath}/js/pay/toss-payments.js?v=3" ></script>

</body>
</html>
