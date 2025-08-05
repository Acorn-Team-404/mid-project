<%@page import="model.page.StayDto"%>
<%@page import="model.page.StayDao"%>
<%@page import="model.user.UserDto"%>
<%@page import="model.user.UserDao"%>
<%@page import="model.page.PageDao"%>
<%@page import="model.page.PageDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	// 숙소, 유저는 세션 객체로부터 얻어낸다
	String stay_name = (String) session.getAttribute("stay_name");
	Long users_num = (Long) session.getAttribute("usersNum");
	
	String usersId = (String) session.getAttribute("usersId");
	UserDto dto2 = null;
	if (usersId != null) {
	    dto2 = UserDao.getInstance().getByUserId(usersId);
	} else {
	    // 사용자 정보 없으면 로그인 페이지로 이동하거나 에러 처리
	    out.println("<script>alert('로그인이 필요합니다.'); location.href='/user/login-form.jsp';</script>");
	    return;
	}
	
	// 폼 전송
	long stay_num = Long.parseLong(request.getParameter("stayNum"));
	String pageContent = request.getParameter("content");
	String pageReserve = request.getParameter("notice_reserve");
	String pageGuide = request.getParameter("notice_guide");
	String pageRefund = request.getParameter("notice_refund");
	
	// 페이지 번호를 미리 얻어낸다
	long page_num=PageDao.getInstance().getSequence();
	
	// DB 에 저장하기
	PageDto dto = new PageDto();
	dto.setStayNum(stay_num);
	dto.setUsersNum(users_num);
	dto.setPageContent(pageContent);
	dto.setPageReserve(pageReserve);
	dto.setPageGuide(pageGuide);
	dto.setPageRefund(pageRefund);
	
	// 글 번호도 dto 에 담는다
	dto.setPageNum(page_num);
	
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