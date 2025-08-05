<%@page import="model.post.CommentDto"%>
<%@page import="java.util.List"%>
<%@page import="model.post.CommentDao"%>
<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // PostDto dto = (PostDto) request.getAttribute("dto");
    
    
  	//자세히 보여줄 gallery 의 pk
  	int num= Integer.parseInt(request.getParameter("num"));
  	//gallery 정보 얻어오기
  	PostDto dto=PostDao.getInstance().getByPostNum(num);
  	//gallery에 업로드된 이미지 목록 얻어오기
  	//List<GalleryImageDto> images = GalleryDao.getInstance().getImageList(num);
  	//로그인된 userName (null일 가능성 있음)
  	Long usersNum = (Long) session.getAttribute("usersNum");
  	String writer = (String) session.getAttribute("usersId");
  	if(writer == null) {
  	    // 로그인하지 않은 사용자는 댓글 작성 불가
  	    response.sendRedirect(request.getContextPath() + "/user/login-form.jsp");
  	    return;
  	}
  	//댓글 목록
  	List<CommentDto> commentList = CommentDao.getInstance().selectAll(num);
  	//로그인 여부
  	//boolean isLogin = usersNum == null ? false : true;
  	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/post/view.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp">
		<jsp:param value="view" name="thisPage"/>	
	</jsp:include>
	
	<%if(dto==null){ %>
		<div class="container">
			<div class="alert alert-danger mt-5">
				해당 게시글을 찾을 수 없습니다.
			</div>
		</div>
	<%} %>
	
	
	<!-- 이미지..경로 필요!!! -->
	<jsp:include page="/WEB-INF/include/index-carousel.jsp"></jsp:include>
	
	<div class="container">
	
		<div> <!-- 제목 -->
			<h1 class="text-center my-4"><%=dto.getPostTitle() %></h1>
		</div>
		
		<!-- 본문 -->
		<div class="card-body">
			<p class="card-text"><%=dto.getPostContent().replaceAll("\n", "<br>") %></p>
		</div>

			<!-- 게시글 정보 -->		
			<div class="container col-10 mt-5"> 

				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">글 번호</div>
				  <div class="col-6 text-end"><%= dto.getPostNum() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">작성자</div>
				  <div class="col-6 text-end"><%= dto.getUsersID() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">숙소 번호</div>
				  <div class="col-6 text-end"><%= dto.getPostStayNum() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">조회수</div>
				  <div class="col-6 text-end"><%= dto.getPostViews() %></div>
				</div>
				
				<div class="row border-bottom py-2">
				  <div class="col-6 fw-semibold">작성일</div>
				  <div class="col-6 text-end"><%= dto.getPostCreatedAt() %></div>
				</div>
				
				<!-- 숨기고 싶은 값 -->
				<div class="d-none">
				  <%= dto.getPostType() %>
				</div>
				
			</div> <!-- 게시글 정보 -->	
			
			<div>
				<%if(dto.getPostWriterNum().equals(usersNum)){ %>
				<div class="text-end pt-2">
					<a class="btn btn-warning btn-sm" href="update.post?num=<%=dto.getPostNum()%>">Edit</a>
					<a class="btn btn-danger btn-sm" href="delete.post?num=<%=dto.getPostNum()%>">Delete</a>
				</div>
				<%} %>
			</div>
		
		
			<!-- 댓글 -->
		<div class="card my-3">
		  <div class="card-header bg-primary text-white">
		    댓글을 입력해 주세요
		  </div>
		  <div class="card-body">
		    <!-- 원글의 댓글을 작성할 폼 -->
		    <form action="save-comment.jsp" method="post">
		      <!-- 숨겨진 입력값 -->
		      <input type="hidden" name="parentNum" value="<%=dto.getPostNum() %>"/>
		      <input type="hidden" name="targetWriter" value="<%=dto.getPostWriterNum() %>" />
		
		      <div class="mb-3">
		        <label for="commentContent" class="form-label">댓글 내용</label>
		        <textarea id="commentContent" name="content" rows="5" class="form-control" placeholder="댓글을 입력하세요"></textarea>
		      </div>
		
		      <button type="submit" class="btn btn-success">등록</button>
		    </form>
		  </div>
		</div>
		
		<!-- 댓글 목록 출력하기 -->
		<div class="comments">
			<%for(CommentDto tmp:commentList){ %>
			<!-- 대댓글은 자신의 글번호와 댓글의 그룹번호가 다름->왼쪽 마진(들여쓰기) -->
				<div class="card mb-3 <%=tmp.getCommentNum() == tmp.getCommentGroupNum() ? "" : "ms-5 re-re" %>">
					<%if(tmp.getCommentDeleted().equals("yes")){ %>
						<div class="card-body">삭제된 댓글입니다.</div>
					<%}else{ %>
						<div class="card-body d-flex flex-column flex-sm-row position-relative">
							<%-- <%if(tmp.getReplyCount() != 0 && tmp.getCommentNum() == tmp.getCommentGroupNum()){ %>
			            		<button class="dropdown-btn btn btn-outline-secondary btn-sm position-absolute"
			            			style="bottom:16px; right:16px;">
			            			<i class="bi bi-caret-down"></i>
			            			답글 <%=tmp.getReplyCount() %> 개
			            		</button>
		            		<%} %> --%>
		            		
							<%if(tmp.getCommentNum() != tmp.getCommentGroupNum()){ %>
		            			<i class="bi bi-arrow-return-right position-absolute" style="top:0;left:-30px"></i>
		            		<%} %>
			            	<!-- 댓글 작성자가 로그인된 userName일 경우 삭제버튼 출력 -->
			            	<%if(tmp.getCommentWriter().equals(writer)) {%>
			            		<button data-num="<%=tmp.getCommentNum() %>" class="btn-close position-absolute top-0 end-0 m-2"></button>
			            	<%} %>
			            	<%-- <%if(tmp.getProfileImage()==null){ %>
			            		<i style="font-size:50px;" class="bi me-3 align-self-center bi-person-circle"></i>
			            	<%}else{ %>
				                <img class="rounded-circle me-3 align-self-center" 
				                	src="${pageContext.request.contextPath }/upload/<%=tmp.getProfileImage() %>" 
				                	alt="프로필 이미지" 
				                	style="width:50px; height:50px;">
			                <%} %> --%>
		                		                
			                <div class="flex-grow-1">
			                    <div class="d-flex justify-content-between">
			                        <div>
			                            <strong><%=tmp.getCommentWriter() %></strong>
			                            <span>@<%=tmp.getCommentTargetWriter() %></span>
			                        </div>
			                        <small><%=tmp.getCommentCreatedAt() %></small>
		                    	</div>
		                    	<pre><%=tmp.getCommentContent() %></pre>
		                    	<!-- 댓글 작성자가 로그인된 userName이라면 수정폼 / 아니면 댓글폼 -->
		                    	<%if(tmp.getCommentWriter().equals(writer)){ %>
		                    		<!-- 수정 버튼 (본인에게만 보임) -->
				                    <button class="btn btn-sm btn-outline-secondary edit-btn">수정</button>
				
				                    <!-- 댓글 수정 폼 (처음에는 숨김) -->
				                    <div class="d-none form-div">
				                        <form action="comment-update.jsp" method="post">
					                        <!-- 댓글을 수정하기 위한 댓글의 번호, 이 페이지로 다시 돌아오기 위한 parentNum 같이 전송 -->
					                        <input type="hidden" name="num" value="<%=tmp.getCommentNum()%>"/>
					                        <input type="hidden" name="parentNum" value="<%=num%>"/>
				                            <textarea name="content" class="form-control mb-2" rows="2" ><%=tmp.getCommentContent() %></textarea>
				                            <button type="submit" class="btn btn-sm btn-success">수정 완료</button>
				                            <button type="reset" class="btn btn-sm btn-secondary cancel-edit-btn">취소</button>
			                        	</form>
	                    			</div>	
		                    	
		                    	<%}else{ %>
			                    	<button class="btn btn-sm btn-outline-primary show-reply-btn">댓글</button>  
		                   			<!-- 댓글 입력 폼 (처음에는 숨김) -->
		                   			<div class="d-none form-div">
		                       			<form action="save-comment.jsp" method="post">
		                       				<!-- 원글의 글 번호, 댓글대상자, 댓글 그룹번호 -->
		                       				<input type="hidden" name="parentNum" value="<%=tmp.getCommentNum() %>" />
		                       				<input type="hidden" name="targetWriter" value="<%=tmp.getCommentWriter() %>" />
		                       				<input type="hidden" name="groupNum" value="<%=tmp.getCommentGroupNum() %>" />
		                           			<textarea name="content" class="form-control mb-2" rows="2" placeholder="댓글을 입력하세요..."></textarea>
		                           			<button type="submit" class="btn btn-sm btn-success">등록</button>
		                           			<button type="reset" class="btn btn-sm btn-secondary cancel-reply-btn">취소</button>
		                           		</form>
		                    		</div>
		                    	<%} %>
	                    	</div>
                 		</div> <!-- card body -->
					<%} %>
               	</div> <!-- card -->
			<%} %>
		</div> <!-- comments -->
		
	</div> <!-- container -->	
</body>
</html>