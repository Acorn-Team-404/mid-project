<%@page import="model.page.StayDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String usersId=(String)session.getAttribute("usersId");
    String strStayNum = request.getParameter("stayNum");
    long stayNum = 0;
    if(strStayNum == null || strStayNum.trim().equals("")) {
%>
<script>
    alert("잘못된 접근입니다.");
    history.back();
</script>
<%
        return;
    }
    try {
        stayNum = Long.parseLong(strStayNum);
    } catch(NumberFormatException e) {
%>
<script>
    alert("숙소 번호가 올바르지 않습니다.");
    history.back();
</script>
<%
        return;
    }

    StayDto dto = StayDao.getInstance().getByNum(stayNum);

    if(dto == null) {
%>
<script>
    alert("숙소 미등록");
    history.back();
</script>
<%
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/stay/stay-view</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
    <div class="container mt-4">
        <h2>숙소 상세 정보</h2>
        <p><strong>숙소 코드:</strong><%=dto.getStayNum() %></p>
        <p><strong>숙소 관리자:</strong><%=dto.getUsersId() %></p>
        <p><strong>숙소 이름:</strong><%=dto.getStayName() %></p>
        <p><strong>지역:</strong><%=dto.getStayLoc() %></p>
        <p><strong>주소:</strong><%=dto.getStayAddr() %></p>
        <p><strong>대표 전화:</strong><%=dto.getStayPhone() %></p>
        <p><strong>편의시설:</strong><%=dto.getStayFacilities() != null ? dto.getStayFacilities() : "없음" %></p>
    </div>
</body>
</html>