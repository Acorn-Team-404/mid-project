<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String paymentKey = request.getParameter("paymentKey");
  String orderId = request.getParameter("orderId");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>결제 성공</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background-color: #f8f9fa;
      font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif;
    }
    .container {
      max-width: 720px;
      margin-top: 80px;
    }
    .card {
      border: none;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      padding: 40px;
      text-align: center;
    }
    .card h2 {
      font-size: 1.8rem;
      font-weight: 600;
      margin-bottom: 30px;
    }
    .info {
      font-size: 1.1rem;
      margin-bottom: 10px;
    }
    .btn-dark {
      padding: 12px 0;
      font-size: 1rem;
      border-radius: 8px;
    }
  </style>
</head>
<body>

<div class="container">
  <div class="card">
    <h2>결제가 완료되었습니다.</h2>
    <p class="info"><strong>결제 키:</strong> <%= paymentKey %></p>
    <p class="info"><strong>주문 번호:</strong> <%= orderId %></p>
    <p>예약한 숙소 명</p>
    <p>예약한 방번호</p>
    <p>예약한 인원 수</p>
    <p>예약한 침대 수건 픽업여부 등등</p>
    <p>고객 요청사항</p>
    <p>최종 결제 금액</p>

    <!-- 버튼 영역 -->
    <div class="row mt-4">
      <div class="col-6">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-dark w-100">홈으로</a>
      </div>
      <div class="col-6">
        <a href="${pageContext.request.contextPath}/my-page.jsp" class="btn btn-dark w-100">예약내역 보기</a>
      </div>
    </div>

  </div>
</div>

</body>
</html>
