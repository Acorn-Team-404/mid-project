<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.tomcat.jakartaee.commons.lang3.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="model.inq.InquiryDao"%>
<%@page import="model.inq.InquiryDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String startDate=request.getParameter("startDate");
	String endDate=request.getParameter("endDate");
	Long usersNum=(Long)session.getAttribute("usersNum");
	String uri=request.getContextPath()+"/inquiry/list.jsp";
	if (usersNum == null) {
	    response.sendRedirect(request.getContextPath()+"/user/login-form.jsp?url="+URLEncoder.encode(uri, "UTF-8"));
	    return;
	}
	
	long pageNum=1;
	long totalRow=0;
	String strPageNum=request.getParameter("pageNum");
	if(strPageNum != null){
		pageNum=Long.parseLong(strPageNum);
	}
	final long PAGE_ROW_COUNT=5;
	final long PAGE_DISPLAY_COUNT=5;
	
	long startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT;
	long endRowNum=pageNum*PAGE_ROW_COUNT;
	
	long startPageNum = 1 + ((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
	long endPageNum=startPageNum+PAGE_DISPLAY_COUNT-1;
	
	if(!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)){
		totalRow=InquiryDao.getInstance().getCountByCreatedAt(usersNum, startDate, endDate);
	} else {
		totalRow=InquiryDao.getInstance().getCountByUser(usersNum);
	}
	
	long totalPageCount=(long)Math.ceil((double)totalRow/PAGE_ROW_COUNT);
	if(endPageNum > totalPageCount){
		endPageNum=totalPageCount;
	}
	
	InquiryDto dto=new InquiryDto();
	dto.setStartRowNum(startRowNum);
	dto.setEndRowNum(endRowNum);
	dto.setUsersNum(usersNum);

	List<InquiryDto> list=null;
	if(!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)){
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		list=InquiryDao.getInstance().selectPageByCreatedAt(dto);
	} else {
		list=InquiryDao.getInstance().selectPageByUser(dto);
	}
	
	String queryParam = "";
	if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
		queryParam = "&startDate=" + startDate + "&endDate=" + endDate;
	}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>/inquiry/list.jsp</title>
    <jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
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
        .no-border {
        	border-top: none !important;
		    border-bottom: none !important;
		}
    </style>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="modal fade" id="alertModal" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
	    	<div class="modal-content">
	    		<div class="modal-header no-border">
	       			<h5 class="modal-title">알림</h5>
	        		<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	      		</div>
	      		<div class="modal-body" id="alertModalBody">
	        		<!-- 메시지 삽입 -->
	      		</div>
	      		<div class="modal-footer no-border">
	        		<button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
	      		</div>
	    	</div>
	  	</div>
	</div>
    <div class="container">
        <div class="text-center mt-5">
            <h1>1:1 문의 내역</h1>
            <a class="btn btn-primary btn-sm mt-5" href="new-inquiry.jsp">
                문의하기
            </a>
        </div>
        <div class="mt-4 mb-3">
            <form action="list.jsp" method="get" id="searchForm"
                class="row gy-2 gx-3 align-items-center">
                <div class="col-6 d-flex pe-2">
                	<div class="d-flex">
	                    <label class="form-label d-none" for="startDate">시작일</label>
	                    <input type="date" class="form-control form-control-sm me-2" id="startDate" name="startDate"
	                        value="<%=StringUtils.isEmpty(startDate) ? "" : startDate %>">
	                    <label class="form-label d-none" for="endDate">종료일</label>
	                    <input type="date" class="form-control form-control-sm" id="endDate" name="endDate"
	                    	value="<%=StringUtils.isEmpty(endDate) ? "" : endDate%>">
	                </div>
	                <div class="col-auto">
	                    <button type="submit" class="btn btn-sm btn-outline-primary ms-2">검색</button>
	                    <button type="button" class="btn btn-sm btn-outline-primary ms-1" id="clearBtn">전체보기</button>
	                </div>
				</div>
            </form>
        </div>
        <div class="p-1 mw-100 border-2 rounded-3">
            <div class="d-flex align-items-center row text-center p-3">
                <span class="col-2">번호</span>
                <span class="col-2">유형</span>
                <span class="col">제목</span>
                <span class="col-2">문의일</span>
                <span class="col-2">상태</span>
            </div>
            <ul class="list-unstyled p-0 m-0 rounded shadow-sm overflow-hidden">
            	<%for(InquiryDto tmp:list){ %>
	            	<li>
	            		<div class="inq-list d-flex align-items-center text-center p-3">
		                    <span class="col-2"><%=tmp.getNum() %></span>
		                    <span class="col-2"><%=tmp.getType() %></span>
		                    <span class="col"><%=tmp.getTitle() %></span>
		                    <span class="col-2"><%=tmp.getCreatedAt() %></span>
		                    <span class="col-2">
		                    	<%if(tmp.getIsAnswered()==0) {%>
		                    		<span class="badge bg-warning">처리중</span>
		                    	<%}else {%>
		                        	<span class="badge bg-success">답변완료</span>
		                        <%} %>
		                        <span class="toggle-icon">▼</span>
		                    </span>
						</div>
	                    <div class="answer-box p-4 d-none shadow-sm">
		                    <div>
		                        <p class="fs-5 mb-0">Q</p>
		                        <strong class="small mb-1"><%=tmp.getStayName()==null||tmp.getStayName().isEmpty() ? "기타" : tmp.getStayName() %></strong>
		                        <p class="border-bottom pb-4"><%=tmp.getContent().replaceAll("\n", "<br>") %></p>
		                    </div>
		                    <div class="<%=tmp.getIsAnswered()==0 ? "d-none" : "" %> ">
		                        <p class="fs-5 mb-0">A</p>
		                        <p><%=tmp.getAnswer() == null ? "" : tmp.getAnswer().replaceAll("\n", "<br>") %></p>
		                        <p class="text-end small text-muted"><%=tmp.getAnsweredAt() %></p>
		                    </div>
	                	</div>
	                </li>
                <%} %>
			</ul>
			<ul class="pagination justify-content-center mt-3">
				<%-- startPageNum 이 1이 아닐때 이전 page 가 존재하기 때문에... --%>
				<%if(startPageNum != 1){ %>
					<li class="page-item">
						<a class="page-link" href="list.jsp?pageNum=<%=startPageNum-1 %>&startDate=<%=startDate %>&endDate=<%=endDate%>">&lsaquo;</a>
					</li>
				<%} %>			
				<%for(long i=startPageNum; i<=endPageNum ; i++){ %>
					<li class="page-item">
						<a class="page-link <%= i==pageNum ? "active":"" %>" href="list.jsp?pageNum=<%=i %><%=queryParam %>"><%=i %></a>
					</li>
				<%} %>
				<%-- endPageNum 이 totalPageCount 보다 작을때 다음 page 가 있다 --%>		
				<%if(endPageNum < totalPageCount){ %>
					<li class="page-item">
						<a class="page-link" href="list.jsp?pageNum=<%=endPageNum+1 %>&startDate=<%=startDate %>&endDate=<%=endDate%>">&rsaquo;</a>
					</li>
				<%} %>	
			</ul>
        </div>
    </div>
    <jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
    <script>
	    const showAlertModal = (message) => {
	        const modalBody = document.getElementById("alertModalBody");
	        modalBody.textContent = message;
	        const modal = new bootstrap.Modal(document.getElementById("alertModal"));
	        modal.show();
	    };
        document.querySelectorAll(".inq-list").forEach(item => {
            item.addEventListener("click", () => {
                item.classList.toggle("active")
                item.nextElementSibling.classList.toggle("d-none")
            });
        });
        document.querySelector("#startDate").addEventListener("change", (e)=>{
            const start = document.querySelector("#startDate").value.trim();
            const end = document.querySelector("#endDate").value.trim();
            if (start && end) {
                const startDate = new Date(start);
                const endDate = new Date(end);

                if (startDate > endDate) {
                    e.preventDefault();
                    showAlertModal("시작일은 종료일과 같거나 이전이어야 합니다.");
                    return;
                }
            }
        });
        document.querySelector("#endDate").addEventListener("change", (e)=>{
            const start = document.querySelector("#startDate").value.trim();
            const end = document.querySelector("#endDate").value.trim();
            if (start && end) {
                const startDate = new Date(start);
                const endDate = new Date(end);

                if (startDate > endDate) {
                    e.preventDefault();
                    showAlertModal("시작일은 종료일과 같거나 이전이어야 합니다.");
                    return;
                }
            }
        });
        document.querySelector("#searchForm").addEventListener("submit", (e) => {
            const start = document.querySelector("#startDate").value.trim();
            const end = document.querySelector("#endDate").value.trim();
            if (!start) {
            	e.preventDefault();
            	showAlertModal("시작일을 선택해주세요.");
            	return;
            }
            if (!end) {
            	e.preventDefault();
            	showAlertModal("종료일을 선택해주세요.");
            	return;
         	}
        });
        
        document.querySelector("#clearBtn").addEventListener("click", (e)=>{
        	document.querySelector("#startDate").value = "";
            document.querySelector("#endDate").value = "";
            document.querySelector("form").submit();
        });
    </script>
</body>

</html>