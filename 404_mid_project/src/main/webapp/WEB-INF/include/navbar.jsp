<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/include/navbar.jsp --%>
<%
	//String navUserId = "null";
	String usersId = (session != null) ? (String) session.getAttribute("usersId") : null;
	request.setAttribute("usersId", usersId);
%>
<nav class="navbar navbar-expand-lg bg-white border-bottom shadow-sm py-3">
  <div class="container-fluid align-items-center flex-nowrap">
    <!-- 좌측 로고 -->
    <a class="navbar-brand fw-bold" href="#">STAYLOG</a>

    <!-- 검색창 -->
    <form class="d-flex mx-4 flex-grow-1" role="search">
      <div class="input-group rounded-pill border bg-light px-3" style="height: 45px; width: 100%;">
        <span class="input-group-text bg-transparent border-0">
          <i class="bi bi-search"></i> <!-- Bootstrap Icons 사용 -->
        </span>
        <input type="search" class="form-control border-0 bg-transparent fw-semibold" placeholder="어디로 떠날까요?" aria-label="Search">
      </div>
    </form>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav">
			<span class="navbar-toggler-icon"></span>
		</button>

    <!-- 우측 메뉴(collapse) -->
    <div class="collapse navbar-collapse" id="navbarNav">
	    <ul class="navbar-nav align-items-center ms-auto fw-semibold">
	      <li class="nav-item mx-2">
	     	 <a class="nav-link text-dark text-nowrap" href="${pageContext.request.contextPath}/user/login-form.jsp">LOGIN</a>
	      </li>
	      <li class="nav-item mx-2">
	     	 <a class="nav-link text-dark text-nowrap" href="list.post">JOURNAL</a>
	      </li>
	      <li class="nav-item mx-2">
	     	 <a class="nav-link text-dark text-nowrap" href="${pageContext.request.contextPath}/my-page">MYPAGE</a>
	      </li>
	      <li class="nav-item mx-2">
	     	 <a class="nav-link text-dark text-nowrap" href="${pageContext.request.contextPath}/pay/payments.jsp">PAY</a>
	      </li>
	      <li class="nav-item mx-2">
	      	<a class="nav-link text-dark text-nowrap" href="${pageContext.request.contextPath}/dbtest">DBTest</a>
	      </li>
	      <li class="nav-item mx-3">
	        <div class="vr"></div> <!-- 세로 구분선 -->
	      </li>
	      <li class="nav-item mx-2">
	      	<% if(usersId != null) {%>
	      		<!-- 알림창 버튼 (로그인 세션 있을때만 출력) -->
	          <button class="btn btn-dark" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasWithBothOptions" aria-controls="offcanvasWithBothOptions"><i class="bi bi-bell"></i> 알림</button>
	      	<%} %>
	      </li>
	      <li class="nav-item mx-2">
	        <a class="nav-link text-dark d-flex align-items-center text-nowrap" href="${pageContext.request.contextPath}/user/signup-form.jsp">
	          <i class="bi bi-person me-1"></i> 회원가입
	        </a>
	      </li>
	    </ul>
    </div> <!-- end collapse -->
  </div>
</nav>


<!-- notification-modal.jsp (알림창 모달 include) -->
<jsp:include page="/WEB-INF/include/notification-modal.jsp"></jsp:include>




