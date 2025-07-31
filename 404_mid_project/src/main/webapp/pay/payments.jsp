<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <title>결제 페이지</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="icon" href="https://static.toss.im/icons/png/4x/icon-toss-logo.png" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://js.tosspayments.com/v2/standard"></script>
  <style>
    body {
      background-color: #f8f9fa;
      font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif;
    }
    .container {
      max-width: 720px;
      margin-top: 60px;
    }
    .card {
      border: none;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.06);
      padding: 40px;
    }
    .title {
      font-size: 1.5rem;
      font-weight: 600;
      margin-bottom: 1rem;
    }
    .info {
      font-size: 1.1rem;
      margin-bottom: 0.5rem;
    }
    .button2 {
      flex: 1;
      margin: 0 5px;
      padding: 12px;
      border-radius: 8px;
      border: 1px solid #ced4da;
      background-color: white;
      cursor: pointer;
      transition: all 0.2s ease-in-out;
    }
    .button2:hover {
      background-color: #e9ecef;
    }
    .button {
      width: 100%;
      padding: 14px;
      font-size: 1.1rem;
      background-color: #000;
      color: #fff;
      border: none;
      border-radius: 8px;
      transition: background-color 0.3s;
    }
    .button:hover {
      background-color: #333;
    }
    #payment-method {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
    }
  </style>
</head>

<body>
  <div class="container">
    <div class="card">
      <!-- 예약 정보 -->
      <div class="title">예약 정보</div>
      <p class="info"><strong>숙소:</strong> 그랜드 하얏뜨</p>
      <p class="info"><strong>결제 금액:</strong> 100,000원</p>
      <%-- 예: 금액은 자바단에서 TO_CHAR로 포맷팅해서 전달 가능 --%>

      <hr class="my-4">

      <!-- 결제 수단 선택 -->
      <h5 class="mb-3">결제 수단</h5>
      <div id="payment-method">
        <button id="CARD" class="button2" onclick="selectPaymentMethod('CARD')">카드 및 간편 결제</button>
        <button id="TRANSFER" class="button2" onclick="selectPaymentMethod('TRANSFER')">계좌이체</button>
        <button id="MOBILE_PHONE" class="button2" onclick="selectPaymentMethod('MOBILE_PHONE')">휴대폰</button>
      </div>

      <!-- 결제하기 버튼 -->
      <button class="button mt-4" onclick="requestPayment()">결제하기</button>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/js/pay/toss-payments.js"></script>
</body>
</html>
