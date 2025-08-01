<%@page import="model.page.PageDao"%>
<%@page import="model.page.PageDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	// 폼 전송
	String content = request.getParameter("content");
	String reserve = request.getParameter("notice_reserve");
	String guide = request.getParameter("notice_guide");
	String refund = request.getParameter("notice_refund");

	// 글 작성자, 숙소는 세션 객체로부터 얻어낸다
	String stay_name = (String)session.getAttribute("stay_name");
	String user_name = (String)session.getAttribute("user_name");
	long stay_num = (Long)session.getAttribute("stay_num");
	long user_num = (Long)session.getAttribute("user_num");
	
	// DB 에 저장하기
	PageDto dto = new PageDto();
	dto.setPage_content(content);
	dto.setPage_reserve(reserve);
	dto.setPage_guide(guide);
	dto.setPage_refund(refund);
	dto.setStay_num(stay_num);
	dto.setUser_num(user_num);

	boolean isSuccess = PageDao.getInstance().insert(dto);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/save</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<% if(isSuccess){ %>
		<script>
			alert("페이지 정보가 저장되었습니다.");
			location.href="view.jsp?stay_num=<%=stay_num %>";
		</script>
	<% } else { %>
		<script>
			alert("저장 실패");
		</script>
		<p><a href="new-form.jsp">다시 작성하기</a></p>
	<% } %>
</body>
</html>