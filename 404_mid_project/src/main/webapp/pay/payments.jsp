<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <link rel="icon" href="https://static.toss.im/icons/png/4x/icon-toss-logo.png" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/payments/paymensts.css" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>/payments/payments.jsp</title>
    <!-- SDK 추가 -->
    <script src="https://js.tosspayments.com/v2/standard"></script>
  </head>

  <body>
  	<!-- 예약 정보 영역 추후에 예약페이지에서 받은 정보 넣어줌-->
  	<div class="title">예약 정보</div>
  	<p>그랜드 하얏뜨</p>
	<p>결제 금액: 십마넌</p>
  	
    <!-- 주문서 영역 -->
    <div class="wrapper">
      <div class="box_section" style="padding: 40px 30px 50px 30px; margin-top: 30px; margin-bottom: 50px">
        <h1>일반 결제</h1>
        <!-- 결제 UI -->
        <div id="payment-method" style="display: flex">
          <button id="CARD" class="button2" onclick="selectPaymentMethod('CARD')">카드</button>
          <button id="TRANSFER" class="button2" onclick="selectPaymentMethod('TRANSFER')">계좌이체</button>
          <button id="MOBILE_PHONE" class="button2" onclick="selectPaymentMethod('MOBILE_PHONE')">휴대폰</button>
        </div>
        <!-- 결제하기 버튼 -->
        <button class="button" style="margin-top: 30px" onclick="requestPayment()">결제하기</button>
      </div>
    </div>
    
  </body>
  <!-- 분리한 js파일 경로로 GOGO -->
  <script src= "${pageContext.request.contextPath}/js/pay/toss-payments.js"></script> 
  </html>