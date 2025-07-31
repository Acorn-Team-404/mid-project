<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/booking/booking-confirm.jsp</title>
</head>
<body>
<div class="container">
    <h1>예약 내역</h1>
	
	<div class="section stay-info">
        <h2>숙소명 </h2>
        <p>숙소 위치: </p>
        <p>숙소 전화번호: </p>
    </div>
    
	<br/>
	
	<div class="section booking-detail">
	<h2>예약 번호 </h2>
    <hr/>
		<div class="row">
			<div>
				<h3>체크인</h3>
				<p>날짜:  </p>
				<p>시간:  </p>
			</div>
			<div>
				<h3>체크아웃</h3>
				<p>날짜: </p>
				<p>시간: </p>
			</div>
			
		</div>
	</div>

	<hr>
	
	<div class="section customer-info">
		<h3>고객 이름: </h3>
		<h3>투숙 인원 </h3>
		<ul>
			<li>성인: 명</li>
			<li>어린이: 명</li>
			<li>유아: 명</li>
		</ul>
		<h3>예약 객실 타입: </h3>
		<h3>요청사항: </h3>
		<p> 요청 내용 </p>
	</div>

	<div class="section payment-info">
		<p>결제 금액: ₩  </p>
		<p class="payment-date">결제 날짜: </p>
	</div>

	<div class="section cancel-policy">
		<h3>취소 규정</h3>
		<p> ㅇㅇ 숙소 취소 규정 </p>
	</div>

	<div class="section guidelines">
		<h3>이용 약관</h3>
		<p> 체크인은 오후4시, 체크아웃은 오전 11시입니다. 최대인원을 초과하는 인원은 입실이 불가합니다. 예약인원 외 방문객의 출입을 엄격히 제한합니다. </p>
	</div>
</div>
</body>
</html>