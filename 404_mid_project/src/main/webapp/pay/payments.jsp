<%@page import="model.book.BookDao"%>
<%@page import="model.book.BookDto"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//String bookNum = (String) request.getAttribute("bookNum");
//	BookDto bookDto = BookDao.getInstance().getByBookNum(bookNum);
	BookDto bookDto = BookDao.getInstance().getByBookNum("20250801-0018");
	System.out.print(bookDto != null ? "예약 정보 로딩 성공" : "예약 정보 없음");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>결제 페이지</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script src="https://js.tosspayments.com/v2/standard"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payments/payments.css">
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="container">
		<div class="card">
			<h2 class="mb-4 text-center">예약 확인 및 결제</h2>


			<div class="section section-box">
				<h2 class="h5">숙소 정보</h2>
				<p>
					<strong>숙소명:</strong> <!-- 예약&숙소 조인 --> 
				</p>
				<p>
					<strong>위치:</strong> <!-- 예약&숙소 조인 -->
				</p>
				<p>
					<strong>전화번호:</strong> <!-- 예약&숙소 조인 -->
				</p>
			</div>

			<div class="section section-box">
				<h2 class="h5">예약 상세</h2>
				<p>
					<strong>예약 번호:</strong> <%= bookDto.getBookNum()%>
				</p>
				<div class="row row-cols-2">
					<div>
						<h3 class="h6">체크인</h3>
						<p>날짜:</p> <%=bookDto.getBookCheckIn() %>
						<p>시간:</p> <%=bookDto.getBookCheckInTime() %>
					</div>
					<div>
						<h3 class="h6">체크아웃</h3>
						<p>날짜:</p><%=bookDto.getBookCheckOut() %>
						<p>시간:</p> 공통 퇴실 시간 ~
					</div>
				</div>
			</div>

			<div class="section section-box">
				<h2 class="h5">투숙객 정보</h2>
				<p>
					<strong>고객 이름:</strong> <!-- 유저테이블조인해서 유저이름 -->
				</p>
				<p>
					<strong>투숙 인원:</strong> <%=bookDto.getBookTotalPax() %>
				</p>
				<ul>
					<li>성인:</li> <%=bookDto.getBookAdult() %>
					<li>어린이:</li><%=bookDto.getBookChildren() %>
					<li>유아:</li> <%=bookDto.getBookInfant() %>
				</ul>
				<p>
					<!-- 객실 테이블과 조인해서 객실타입 가져오기-->
					<strong>객실 타입:</strong> 
				</p>
				<p>
					<strong>요청 사항: </strong> <%=bookDto.getBookRequest()%>
				</p>
			</div>

			<div class="section section-box">
				<h2 class="h5">결제 정보</h2>
				<p>
					<strong>결제 수단:</strong> <!--결제테이블 조인해서 결제수단 -->
				</p>
				<p>
					<strong>금액:</strong> <%=bookDto.getBookTotalAmount() %>
				</p>
				
			</div>

			<div class="section section-box">
				<h2 class="h5">취소 및 이용 안내</h2>
				<p>
					<strong>취소 규정:</strong> ㅇㅇ 숙소 취소 규정
				</p>
				<p>
					<strong>이용 약관:</strong> 체크인은 오후 4시, 체크아웃은 오전 11시입니다. 최대 인원을 초과할 경우
					입실이 제한되며, 외부 방문객은 제한됩니다.
				</p>
			</div>

			<div class="section section-box">
				<h2 class="h5 mb-3">결제 수단 선택</h2>
				<div id="payment-method">
					<button id="CARD" class="button2"
						onclick="selectPaymentMethod('CARD')">카드 및 간편 결제</button>
					<button id="TRANSFER" class="button2"
						onclick="selectPaymentMethod('TRANSFER')">계좌이체</button>
					<button id="MOBILE_PHONE" class="button2"
						onclick="selectPaymentMethod('MOBILE_PHONE')">휴대폰 결제</button>
				</div>
				<button class="button mt-4" onclick="requestPayment()">결제하기</button>
			</div>
		</div>
	</div>
	<form action="${pageContext.request.contextPath}/pay/PaymentsServlet" method="post">
    <input type="hidden" name="paymentKey" value="${paymentKey}">
    <input type="hidden" name="orderId" value="${orderId}">
    <input type="hidden" name="amount" value="${amount}">
    <input type="hidden" name="bookNum" value="${param.bookNum}">
	</form>
	<script>
	
		//브라우저 기본 옵션 파란테두리 생성 떄문에 추가함 
  		const buttons = document.querySelectorAll('.button2');
	  
  		buttons.forEach(btn => {
    		btn.addEventListener('click', function () {
      		// 모든 버튼에서 selected 제거
      		buttons.forEach(b => b.classList.remove('selected'));
      		// 현재 클릭한 버튼에 selected 추가
      		this.classList.add('selected');
      		});
    		});
	</script>
<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/pay/toss-payments.js" defer></script>
	
</body>
</html>
