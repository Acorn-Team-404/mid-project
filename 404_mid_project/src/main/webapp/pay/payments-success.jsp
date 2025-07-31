<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String paymentKey = request.getParameter("paymentKey");
  String orderId = request.getParameter("orderId");
    
%>
<html>
<body>
<h2>결제가 성공했습니다!</h2>
<p>결제 키: <%= paymentKey %></p>
<p>주문 번호: <%= orderId %></p>


<!-- 서버 서블릿 호출를 위한 폼 -->
<form action="<%=request.getContextPath()%>/PaymentsServlet" method="post">
  <input type="hidden" name="paymentKey" value="<%= paymentKey %>"/>
  <input type="hidden" name="orderId" value="<%= orderId %>"/>
  <input type="hidden" name="amount" value="10000" />
  <!-- 지금은 결과저장 버튼 누르면 다음페이지로 넘어가고 콘솔에 성공/실패 뜨지만 추후에 DB에 값넣고 상태 전이 되게 해줌 -->
  <input type="submit" value="결과 저장"/>
</form>
</body>
</html> 
