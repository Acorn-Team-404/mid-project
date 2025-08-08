<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/include/navbar.jsp --%>

<%
	// 세션 검증 및 Attribute 저장
	// navbar.jsp를 include한 jsp페이지에서는 request.getAttribute("key")로 세션값 사용 가능
	String usersId = (session != null) ? (String) session.getAttribute("usersId") : null;
	String usersNum = (session != null && session.getAttribute("usersNum") != null)
		    ? session.getAttribute("usersNum").toString()
		    : null;
	request.setAttribute("usersId", usersId);
	request.setAttribute("usersNum", usersNum);
%>

<%-- index에서 include하기 때문에 중복 방지<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include> --%>
<nav class="navbar navbar-expand-lg sticky-top bg-white border-bottom shadow-sm py-3">
  <div class="container-fluid align-items-center">
    <!-- 좌측 로고 -->
    <a class="navbar-brand fw-semibold ms-3" style="letter-spacing: 0.1em;" href="${pageContext.request.contextPath}/index.jsp">STAYLOG</a>

    <!-- 검색창 -->
    <form class="d-flex mx-4 flex-grow-1" role="search">
      <div class="input-group rounded-pill border bg-light px-3 " style="height: 45px; width: 100%;">
        <span class="input-group-text bg-transparent border-0">
          <i class="bi bi-search"></i> <!-- Bootstrap Icons 사용 -->
        </span>
        <input type="search" class="form-control border-0 bg-transparent fw-normal" placeholder="<%=usersId == null ? "" : usersId+"님! " %>어디로 떠날까요?" aria-label="Search">
      </div>
    </form>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav">
			<span class="navbar-toggler-icon"></span>
		</button>

    <!-- 우측 메뉴(collapse) -->
    <div class="collapse navbar-collapse me-3" id="navbarNav">
	    <ul class="navbar-nav align-items-center ms-auto fw-semibold">
	    	<li class="nav-item mx-2 me-5">
	     	 <a class="nav-link text-nowrap fw-normal fw-bold text-primary fs-5" href="${pageContext.request.contextPath}/post/list.jsp">🎉 8월 썸머 프로모션 진행 중! 🎉</a>
	      </li>
	      <li class="nav-item mx-2">

	     	 <a class="nav-link text-nowrap fw-normal text-semiblack" href="${pageContext.request.contextPath}/post/list.jsp">JOURNAL</a>
	      </li>
	      <li class="nav-item mx-2">
	      	<a class="nav-link text-nowrap fw-normal text-semiblack" href="${pageContext.request.contextPath}/dbtest">DBTest</a>
	      </li>
	      <li class="nav-item mx-2">
	      	<a class="nav-link text-nowrap fw-normal text-semiblack" href="${pageContext.request.contextPath}/test/stay-list.jsp">ADMIN</a>
	      </li>
	      <li class="nav-item">
	        <div class="vr"></div> <!-- 세로 구분선 -->
	      </li>
	      <li class="nav-item mx-2">
	      
	      
        <% if (usersId == null) { %>
          <!-- 비로그인 상태일 때 LOGIN & 회원가입 -->
          <li class="nav-item mx-2">
            <a class="nav-link text-dark text-nowrap text-muted" href="${pageContext.request.contextPath}/user/login-form.jsp">LOGIN</a>
          </li>
          <li class="nav-item mx-2">
            <a class="nav-link d-flex align-items-center text-nowrap text-dark fw-bold" href="${pageContext.request.contextPath}/user/signup-form.jsp">
              <i class="bi bi-person-fill me-1 fs-4"></i> 회원가입
            </a>
          </li>
          <li class="nav-item mx-2">
	        <div class="vr"></div> <!-- 세로 구분선 -->
	      </li>
        <% } else { %>
          <!-- 로그인 상태 일 때  알림 & LOGOUT -->
          <li class="nav-item mx-2">
          	<a class="nav-link text-nowrap d-flex align-items-center gap-1" href="${pageContext.request.contextPath}/my-page/my-page.jsp">
	          	<i class="bi bi-person-fill fs-3 text-muted"></i>
	          	<span class="text-semiblack fs-6"><%=usersId %></span> <!-- 현재 세션 상태 확인을 위한 임시코드(지워도 상관없음) -->
          	</a>
          </li>
          <li class="nav-item mx-2">
	        <div class="vr"></div> <!-- 세로 구분선 -->
	      </li>
          <li class="nav-item mx-2">
            <button type="button" class="btn position-relative p-0" data-bs-toggle="offcanvas" data-bs-target="#offcanvasWithBothOptions" aria-controls="offcanvasWithBothOptions">
				  <i class="bi bi-bell-fill fs-4 text-muted"></i>
				  <span class="noti-btn-count position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger px-2"></span>
			</button>
          </li>
          <li class="nav-item mx-2">
	        <div class="vr"></div> <!-- 세로 구분선 -->
	      </li>
          <li class="nav-item mx-2">
            <a class="nav-link text-nowrap text-muted fw-normal fs-7" href="${pageContext.request.contextPath}/logout">
              <i class="bi bi-box-arrow-right me-1 fs-6"></i> LOGOUT
            </a>
          </li>
        <% } %>

	    </ul>
    </div> <!-- end collapse -->
  </div>
</nav>

<% 
	// notification-modal.jsp (알림창 모달 include)
	if (session.getAttribute("usersNum") != null) { %>
		<jsp:include page="/WEB-INF/include/notification-modal.jsp"></jsp:include>
<% } %>




