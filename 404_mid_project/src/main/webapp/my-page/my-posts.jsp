<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//세션에 로그인 된 사용자 번호 가져오고
    Long usersNum = (Long) session.getAttribute("usersNum");
	
	// 만약 로그인 되지 않은 상태면 user/login-form.jsp로 리다이렉트
	if(usersNum == null) {
		response.sendRedirect(request.getContextPath() + "/user/login-form.jsp");
	}
	// 로그인 된 사용자 게시글 목록 가져오기 
	List<PostDto> posts = PostDao.getInstance().getPostUsersNum(usersNum);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/my-page/my-posts.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="container my-5">
		<h2>내 게시글</h2>
		<%-- 조회 된 게시글이 존재한다면 --%>
		<%if(posts != null && !posts.isEmpty()) { %>
			<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
			<%--게시글을 출력한다 --%>
			<% for (PostDto post : posts) {%>
			<tr>
				<td><%=post.getPostNum() %></td>
				<td>
				<a href="<%= request.getContextPath() %>/post/view.post?num=<%= post.getPostNum() %>">
                 	<%= post.getPostTitle() %>
               	</a>
				</td>
				 <td><%= post.getPostCreatedAt().substring(0, 10) %></td> <%-- 작성일 날짜만 출력 (시각 제거) --%>
			</tr>
			<%} %>
			</tbody>
			</table>
		<%}else{ %>
			<div class="alert alert-warning">작성한 게시글이 없습니다.</div>
		<%} %>
		
	</div>
<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
</body>
</html>