<%@page import="model.user.UserDao"%>
<%@page import="model.user.UserDto"%>
<%@page import="model.page.StayDao"%>
<%@page import="java.util.List"%>
<%@page import="model.page.StayDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<StayDto> list = StayDao.getInstance().selectAll();
	
	// 유저 로그인 여부 체크
	String usersId = (String)session.getAttribute("usersId");
	if (usersId == null) {
%>
	<script>
		alert("로그인이 필요합니다.");
		location.href="${pageContext.request.contextPath}/user/login-form.jsp";
	</script>
<%
		return;
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/stay/stay-list</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="container">
		<form action="stay-list.jsp" method="get">
		
		<h1>숙소 관리</h1>
		<p>총 <%=list.size() %>개의 숙소</p>
		<a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/stay/stay-new-form.jsp" >
			<i class="bi bi-plus-lg"></i> 숙소 등록
		</a>
	</div>
	<div class="container">
		<table>
            <thead>
                <tr>
                    <th>No</th>
                    <th>관리자 ID</th>
                    <th>숙소명</th>
                    <th>숙소 주소</th>
                    <th>최종 수정일</th>
                </tr>
            </thead>
            <tbody>
                <%for (StayDto stay : list) {%>
                <tr onclick="location.href='<%=request.getContextPath()%>/stay/stay-view.jsp?stayNum=<%=stay.getStayNum()%>'" style="cursor:pointer;">
                    <td><%=stay.getStayNum() %></td>
                    <td><%=usersId %></td>
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