<%@page import="model.page.PageDto"%>
<%@page import="model.page.PageDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    long pageNum = Long.parseLong(request.getParameter("page_num"));
    PageDto dto = PageDao.getInstance().getByNum(pageNum);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/page-edit</title>
<jsp:include page="/WEB-INF/include/resource.jsp"/>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />

<div class="container mt-5">
    <h2>상세 페이지 수정</h2>
    <form action="page-update.jsp" method="post">
        <input type="hidden" name="page_num" value="<%=dto.getPageNum()%>" />

        <div class="mb-3">
            <label for="pageContent" class="form-label">숙소 소개</label>
            <textarea class="form-control" id="pageContent" name="page_content" rows="4"><%=dto.getPageContent()%></textarea>
        </div>

        <div class="mb-3">
            <label for="pageReserve" class="form-label">예약 안내</label>
            <textarea class="form-control" id="pageReserve" name="page_reserve" rows="4"><%=dto.getPageReserve()%></textarea>
        </div>

        <div class="mb-3">
            <label for="pageGuide" class="form-label">이용 안내</label>
            <textarea class="form-control" id="pageGuide" name="page_guide" rows="4"><%=dto.getPageGuide()%></textarea>
        </div>

        <div class="mb-3">
            <label for="pageRefund" class="form-label">환불 규정</label>
            <textarea class="form-control" id="pageRefund" name="page_refund" rows="4"><%=dto.getPageRefund()%></textarea>
        </div>

        <button type="submit" class="btn btn-primary">수정 완료</button>
    </form>
</div>
</body>
</html>