<%@page import="model.page.StayDao"%>
<%@page import="model.page.StayDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	long stayNum = Long.parseLong(request.getParameter("stay_num"));
	String stayName = request.getParameter("stay_name");
	String stayAddr = request.getParameter("stay_addr");
	String stayLoc = request.getParameter("stay_loc");
	String stayPhone = request.getParameter("stay_phone");
	String stayFacilities = request.getParameter("stay_facilities");
	
	// DB 에 저장하기
	StayDto dto = new StayDto();
	dto.setStayNum(stayNum);
	dto.setStayName(stayName);
	dto.setStayAddr(stayAddr);
	dto.setStayLoc(stayLoc);
	dto.setStayPhone(stayPhone);
	dto.setStayFacilities(stayFacilities);
	
	boolean isSuccess = StayDao.getInstance().update(dto);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/stay/stay-update</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="container mt-4">
    <h2>숙소 수정</h2>
    <form action="stay-update.jsp" method="post">
        <input type="hidden" name="stay_num" value="<%=dto.getStayNum()%>"/>
        
        <label>숙소 이름: <input type="text" name="stay_name" value="<%=dto.getStayName()%>" required /></label><br>
        <label>숙소 주소: <input type="text" name="stay_addr" value="<%=dto.getStayAddr()%>" required /></label><br>
        <label>숙소 지역: <input type="text" name="stay_loc" value="<%=dto.getStayLoc()%>" required /></label><br>
        <label>대표 번호: <input type="text" name="stay_phone" value="<%=dto.getStayPhone()%>" /></label><br>
        <label>편의시설: <input type="text" name="stay_facilities" value="<%=dto.getStayFacilities()%>" /></label><br>

        <button type="submit">수정 완료</button>
    </form>
</div>
</body>
</html>