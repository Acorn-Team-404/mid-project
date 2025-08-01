<%@page import="org.apache.tomcat.jakartaee.commons.lang3.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="model.inq.InquiryDao"%>
<%@page import="model.inq.InquiryDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String startDate=request.getParameter("startDate");
	String endDate=request.getParameter("endDate");
	Long userNum=(Long)session.getAttribute("userNum");
	List<InquiryDto> list=null;
	if(startDate!=null && endDate!=null){
		list=InquiryDao.getInstance().selectByCreatedAt(startDate, endDate);
	}else{
		list=InquiryDao.getInstance().selectAll(userNum);
	}
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/inquiry/list.jsp</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<style>
    .inq-list.active .toggle-icon {
        transform: rotate(180deg);
    }
    .inq-list.active {
        background-color: aliceblue;
    }
    .toggle-icon {
        display: inline-block;
        transition: transform 0.3s ease;
    }
</style>
</head>
<body>
	<div class="container">
		<div class="text-center mt-5">
			<h1>1:1 문의 내역</h1>
			<a class="btn btn-outline-primary btn-sm mt-5" href="${pageContext.request.contextPath }/newInquiry.inq">
				문의하기
			</a>					
		</div>
		<div class="mt-4 mb-3">
			<form action="list.jsp" method="get" class="row gy-2 gx-3 align-items-center">
				<div class="col-6 d-flex pe-2">
			    	<label class="form-label d-none" for="startDate">시작일</label>
			    	<input type="date" class="form-control form-control-sm me-2" id="startDate" name="startDate"
			    			value="<%=StringUtils.isEmpty(startDate) ? "" : startDate %>">
			    	<label class="form-label d-none" for="endDate">종료일</label>
			    	<input type="date" class="form-control form-control-sm" id="endDate" name="endDate"
			    			value="<%=StringUtils.isEmpty(endDate) ? "" : endDate%>">
				</div>
				<div class="col-auto">
					<button type="submit" class="btn btn-sm btn-primary ms-2">검색</button>
				</div>
			</form>
		</div>
		<div class="p-1 mw-100 border-2 rounded-3">
            <div class="d-flex align-items-center row text-center p-3">
                <span class="col-1">번호</span>
                <span class="col-2">유형</span>
                <span class="col">제목</span>
                <span class="col-2">문의일</span>
                <span class="col-2">상태</span>
            </div>
            <ul class="list-unstyled p-0 m-0 rounded shadow-sm overflow-hidden">
            	<%for(InquiryDto tmp:list){ %>
	                <li class="inq-list d-flex align-items-center text-center p-3">
	                    <span class="col-1"><%=tmp.getNum() %></span>
	                    <span class="col-2"><%=tmp.getType() %></span>
	                    <span class="col"><%=tmp.getTitle() %></span>
	                    <span class="col-2"><%=tmp.getCreatedAt() %></span>
	                    <span class="col-2">
	                    	<%if(tmp.getIsAnswered()==0) {%>
	                    		<span class="badge bg-warning ">처리중</span>
	                    	<%}else {%>
	                        	<span class="badge bg-success ">답변완료</span>
	                        <%} %>
	                        <span class="toggle-icon">▼</span>
	                    </span>
	                </li>
	                <div class="p-4 d-none">
	                    <div>
	                        <p class="fs-5 mb-0">Q</p>
	                        <strong class="small"><%=tmp.getStayName()==null||tmp.getStayName().isEmpty() ? "기타" : tmp.getStayName() %></strong>
	                        <p class="border-bottom pb-4">문의 내용</p>
	                    </div>
	                    <div class="<%=tmp.getIsAnswered()==0 ? "d-none" : "" %>">
	                        <p class="fs-5 mb-0">A</p>
	                        <p><%=tmp.getAnswer() %></p>
	                        <p class="text-end small"><%=tmp.getAnsweredAt() %></p>
	                    </div>
	                </div>
                <%} %>
            </ul>
		</div>		
	</div>
	<script>
		document.querySelectorAll(".inq-list").forEach(item=>{
            item.addEventListener("click", ()=>{
                item.classList.toggle("active")
                item.nextElementSibling.classList.toggle("d-none")
            });
        });
	</script>
</body>
</html>