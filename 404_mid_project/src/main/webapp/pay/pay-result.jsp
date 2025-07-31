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
      padding: 12px 24px;
      font-size: 1rem;
      border-radius: 8px;
      margin-top: 30px;
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
	<p>결제 금액</p>
	
	
    <!-- 서버 서블릿 호출를 위한 폼 -->
    <form action="<%=request.getContextPath()%>/PaymentsServlet" method="post">
      <input type="hidden" name="paymentKey" value="<%= paymentKey %>"/>
      <input type="hidden" name="orderId" value="<%= orderId %>"/>
      <input type="hidden" name="amount" value="10000" />
      <!-- 지금은 결과저장 버튼 누르면 다음페이지로 넘어가고 콘솔에 성공/실패 뜨지만 추후에 DB에 값넣고 상태 전이 되게 해줌 -->
      <input type="submit" value="결과 저장" class="btn btn-dark"/>
    </form>
  </div>
</div>

</body>
</html>
