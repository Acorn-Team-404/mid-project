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
</head>
<body>
  <h2>결제에 실패했습니다.</h2>
  <p>오류 코드: <%= errorCode != null ? errorCode : "정보 없음" %></p>
  <p>오류 메시지: <%= errorMessage != null ? errorMessage : "정보 없음" %></p>

  <a href="<%= request.getContextPath()%>/pay/payments.jsp">결제 다시 시도하기</a>
</body>
</html>
