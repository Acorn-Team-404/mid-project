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
</head>
<body>

<div class="container">
  <div class="card">
    <!-- 서버 서블릿 호출를 위한 폼 -->
    <form id = "confirmForm" action="<%=request.getContextPath()%>/pay/PaymentsServlet" method="post">
      <input type="hidden" name="paymentKey" value="<%= paymentKey %>"/>
      <input type="hidden" name="orderId" value="<%= orderId %>"/>
      <input type="hidden" name="amount" value="10000" />
    </form>
    
    <script>
    	document.getElementById("confirmForm").submit();
    </script>
  </div>
</div>

</body>
</html>
