<%@page import="model.user.UserDto"%>
<%@page import="model.user.UserDao"%>
<%@page import="model.page.StayDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.page.PageDao"%>
<%@page import="model.page.PageDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	// 유저 번호를 미리 얻어낸다
	String usersId=(String)session.getAttribute("usersId");
	UserDto userDto = UserDao.getInstance().getByUserId(usersId);
	long usersNum = userDto.getUsersNum();
    long stayNum = StayDao.getInstance().getSequence();
	
	// 폼 전송
	String stayName = request.getParameter("stay_name");
	String stayAddr = request.getParameter("stay_addr");
	String stayLoc = request.getParameter("stay_loc");
	String stayLat = request.getParameter("stay_lat");
	String stayLong = request.getParameter("stay_long");
	String stayPhone = request.getParameter("stay_phone");
	
	// 편의시설 항목들 배열로
	String[] facilities = request.getParameterValues("facilities");
	String stayFacilities = (facilities != null) ? String.join(",", facilities) : "";
	
	// DB 에 저장하기
	StayDto dto = new StayDto();
	dto.setStayNum(stayNum);
  dto.setStayUsersNum(usersNum);
	dto.setStayName(stayName);
	dto.setStayAddr(stayAddr);
	dto.setStayLoc(stayLoc);
	dto.setStayLat(stayLat);
	dto.setStayLong(stayLong);
	dto.setStayPhone(stayPhone);
	dto.setStayFacilities(stayFacilities);
	
	boolean isSuccess = StayDao.getInstance().insert(dto);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/stay/stay-save</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<%if(isSuccess){ %>
		<script>
			alert("숙소 저장 완료 객실 등록 페이지로 이동");
			location.href = "/room/.jsp?stayNum=<%=stayNum %>";
		</script>
	<%}else { %>
		<script>
			alert("숙소 저장 실패");
			history.back();
		</script>
		<p>
			<a href="stay-new-form.jsp">다시 작성하기</a>
		</p>
	<%} %>
</body>
</html>