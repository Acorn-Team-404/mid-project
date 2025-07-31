<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String errorCode = request.getParameter("errorCode");
  String errorMessage = request.getParameter("errorMessage");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>결제 실패</title>
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
      color: #dc3545;
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
    <h2>결제에 실패했습니다.</h2>
    <p class="info"><strong>오류 코드:</strong> <%= errorCode != null ? errorCode : "정보 없음" %></p>
    <p class="info"><strong>오류 메시지:</strong> <%= errorMessage != null ? errorMessage : "정보 없음" %></p>

    <!-- 사용자가 다시 결제 시도할 수 있도록 링크 제공 -->
    <a href="<%= request.getContextPath()%>/pay/payments.jsp" class="btn btn-dark">결제 다시 시도하기</a>
  </div>
</div>

</body>
</html>
