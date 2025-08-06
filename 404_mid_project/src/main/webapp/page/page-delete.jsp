<%@page import="model.page.PageDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// GET 방식 파라미터로 전달되는 글 번호를 읽어 와서
	long num=Long.parseLong(request.getParameter("num"));

	// 글 작성자와 로그인된 usersId 이 동일한지 비교해서 동일하지 않으면 에러를 응답한다
	String usersId=(String)session.getAttribute("usersId");
	
	boolean isSuccess = PageDao.getInstance().deleteByNum(num);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/page-delete</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<script>
		<%if(isSuccess){ %>
			alert("삭제 완료");
			location.href = "${pageContext.request.contextPath}/page/page-list.jsp";
		<%}else{ %>
			alert("삭제 실패");
			history.back();
		<%} %>
	</script>
</body>
</html>