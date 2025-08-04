<%@page import="model.page.PageDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// GET 방식 파라미터로 전달되는 글 번호를 읽어 와서
	long num=Long.parseLong(request.getParameter("num"));

	// 글 작성자와 로그인된 usersId 이 동일한지 비교해서 동일하지 않으면 에러를 응답한다
	String writer=PageDao.getInstance().getByNum(num).getUsersId(); // 삭제할 글 작성자 ★★★ 중복값 불가능한 거 id인지 name인지 학인해야 함 ★★★
	String usersId=(String)session.getAttribute("usersId");
	
	// 만일 글 작성자와 로그인된 usersId 하고 같지 않다면
	if(!writer.equals(usersId)){
		// 에러 페이지 응답
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "본인 글 외에는 삭제할 수 없습니다");
		return; // 메소드를 여기서 종료
	}
	
	// 응답한다
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
			location.href = "<%=request.getContextPath()%>/page/page-list.jsp";
		<%}else{ %>
			alert("삭제 실패");
			history.back();
		<%} %>
	</script>
</body>
</html>