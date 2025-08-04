<%@page import="model.page.StayDao"%>
<%@page import="java.util.List"%>
<%@page import="model.page.StayDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<StayDto> list = StayDao.getInstance().selectAll();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/stay/view</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="container">
		<h1>숙소 관리</h1>
		<p>총 <%=list.size() %>개의 상품</p>
	</div>
	<div class="container">
		<table>
            <thead>
                <tr>
                    <th>No</th>
                    <th>관리자 이름</th>
                    <th>숙소명</th>
                    <th>숙소 주소</th>
                    <th>최종 수정일</th>
                </tr>
            </thead>
            <tbody>
                <%for (StayDto stay : list) { %>
                <tr>
                    <td><%=stay.getStayNum() %></td>
                    <td></td>
                    <td><%=stay.getStayName() %></td>
                    <td><%=stay.getStayAddr() %></td>
                    <td><%=stay.getStayUpdateAt() %></td>
                </tr>
                <%} %>
            </tbody>
        </table>
	</div>
	<script>
		
	</script>
</body>
</html>