<%@page import="model.page.PageDto"%>
<%@page import="model.page.PageDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// 폼 전송되는 내용을 읽어 와서
	long pageNum = Long.parseLong(request.getParameter("pageNum"));
	long stayNum = Long.parseLong(request.getParameter("stayNum"));
    String pageContent = request.getParameter("pageContent");
    String pageReserve = request.getParameter("pageReserve");
    String pageGuide = request.getParameter("pageGuide");
    String pageRefund = request.getParameter("pageRefund");
	
	// 글 작성자와 로그인된 usersId 이 동일한지 비교해서 동일하지 않으면 에러를 응답한다
	String writer=PageDao.getInstance().getByNum(pageNum).getUsersId(); // 삭제할 글 작성자 ★★★ 중복값 불가능한 거 id인지 name인지 학인해야 함 ★★★
	String usersId=(String)session.getAttribute("usersId");
		
	// 만일 글 작성자와 로그인된 usersId 하고 같지 않다면
	if(!writer.equals(usersId)){
		// 에러 페이지 응답
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "본인 글 외에는 수정할 수 없습니다");
		return; // 메소드를 여기서 종료
	}
	
	// DB 에 수정 반영하고
	PageDto dto = new PageDto();
    dto.setPageNum(pageNum);
    dto.setPageContent(pageContent);
    dto.setPageReserve(pageReserve);
    dto.setPageGuide(pageGuide);
    dto.setPageRefund(pageRefund);
	
	// 응답한다
	boolean isSuccess = PageDao.getInstance().update(dto);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/page-update</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<script>
	<%if(isSuccess){ %>
		alert("수정 완료");
		location.href = "<%=request.getContextPath()%>/page/view.jsp?stay_num=<%=stayNum %>";
	<%}else{ %>
		alert("수정 실패");
		history.back();
	<%} %>
	</script>
</body>
</html>