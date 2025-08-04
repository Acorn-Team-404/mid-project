<%@page import="model.faq.FaqDao"%>
<%@page import="model.faq.FaqDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String category=request.getParameter("category");
	List<FaqDto> list=null;
	if(category == null || category.trim().isEmpty()){
		list=FaqDao.getInstance().selectAll();
	}else{
		list=FaqDao.getInstance().getByCategory(category);
	}
	
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>/faq/list.jsp</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
    integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="container">
	    <h1 class="text-center mt-5 mb-5">자주하는 질문</h1>
			<ul class="nav nav-tabs justify-content-center">
				<li class="nav-item">
					<a class="nav-link <%=category == null || category.isEmpty() ? "active" : "" %>" href="list.jsp">전체</a>
				</li>
				<li class="nav-item">
			    	<a class="nav-link <%= "계정".equals(category) ? "active" : "" %>" href="list.jsp?category=계정">계정</a>
				</li>
				<li class="nav-item">
			    	<a class="nav-link <%= "예약".equals(category) ? "active" : "" %>" href="list.jsp?category=예약">예약</a>
				</li>
				<li class="nav-item">
			    	<a class="nav-link <%= "결제/환불".equals(category) ? "active" : "" %>" href="list.jsp?category=<%= java.net.URLEncoder.encode("결제/환불", "UTF-8") %>">결제/환불</a>
				</li>
				<li class="nav-item">
			    	<a class="nav-link <%= "숙소".equals(category) ? "active" : "" %>" href="list.jsp?category=숙소">숙소</a>
				</li>
			</ul>
	    <%for(FaqDto tmp:list) {%>
			<div class="accordion w-100">
		    	<div class="accordion-item border-0 border-bottom">
		        	<h2 class="accordion-header position-relative">
			        	<button class="accordion-button collapsed" type="button" aria-expanded="true">
			            	<span class="fw-bold position-absolute start-0 fs-3">Q</span>
			            	<span class="ms-3"><%=tmp.getQuestion() %></span>
			        	</button>
		        	</h2>
			        <div class="accordion-collapse collapse">
			        	<div class="accordion-body">
			          		<%=tmp.getAnswer().replaceAll("\n", "<br>") %>
			        	</div>
			        </div>
		    	</div>
			</div>
		<%} %>
	</div>
	<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
	<script>
    document.querySelectorAll(".accordion-button").forEach(item => {
    	item.addEventListener("click", (e) => {
        	item.classList.toggle("collapsed")
        	const accCollapse=item.closest(".accordion-item").querySelector(".accordion-collapse");
        	accCollapse.classList.toggle("show")
    	});
    });

    document.querySelectorAll(".nav-link").forEach(item => {
    	item.addEventListener("click", () => {
    		document.querySelectorAll(".nav-link").forEach(e=>{
        		e.classList.remove("active");
        	});
        	item.classList.add("active");
    	})
    });
	</script>
</body>
</html>