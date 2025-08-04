<%@page import="model.page.PageDao"%>
<%@page import="model.page.PageDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	// 폼 전송
	String content = request.getParameter("content");
	String reserve = request.getParameter("notice_reserve");
	String guide = request.getParameter("notice_guide");
	String refund = request.getParameter("notice_refund");
	long stay_num = Long.parseLong(request.getParameter("stay_num"));

	// 글 작성자, 숙소는 세션 객체로부터 얻어낸다
	String stay_name = (String)session.getAttribute("stay_name");
	String users_name = (String)session.getAttribute("users_name");
	long users_num = (Long)session.getAttribute("users_num");
	
	
	// DB 에 저장하기
	PageDto dto = new PageDto();
	dto.setStayNum(stay_num);
	dto.setUsersNum(users_num);
	dto.setPageContent(content);
	dto.setPageReserve(reserve);
	dto.setPageGuide(guide);
	dto.setPageRefund(refund);

	boolean isSuccess = PageDao.getInstance().insert(dto);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/page-save</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<%if(isSuccess){ %>
		<script>
			alert("페이지 정보가 저장되었습니다.");
			location.href="page-view.jsp?stay_num=<%=stay_num %>";
		</script>
	<%}else{ %>
		<script>
			alert("저장 실패");
		</script>
		<p><a href="page-new-form.jsp">다시 작성하기</a></p>
	<%} %>
</body>
</html>