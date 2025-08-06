<%@page import="model.post.CommentDto"%>
<%@page import="model.post.CommentDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	//폼 전송되는 댓글 정보를 얻어낸다.
	int parentNum=Integer.parseInt(request.getParameter("parentNum"));
	int targetWriter=Integer.parseInt(request.getParameter("targetWriter"));
	String content=request.getParameter("content");
	
	// 댓글의 그룹번호가 넘어오는지 읽어온다. (null이면 원글의 댓글 저장 요청)
	String strGroupNum=request.getParameter("groupNum");
	//댓글의 그룹번호를 담을 변수를 미리 만들고
	int groupNum=0;
	//만일 댓글 그룹번호가 넘어온다연 (대댓이라면)
	if(strGroupNum !=null){
		//댓글의 그룹번호를 변수에 담는다.
		groupNum=Integer.parseInt(strGroupNum);
	}
	
	
	// 댓글 작성자 정보는 session 으로부터 얻어낸다.
	// String writer=(String)session.getAttribute("userNum");
	Long usersNum = (Long)session.getAttribute("usersNum");
	int writer=usersNum.intValue();
	//저장할 댓글의 글번호를 미리 얻어낸다.
	int num=CommentDao.getInstance().getSequence();
	
	// DTO 구성
	CommentDto dto = new CommentDto();
	dto.setCommentNum(num);
	dto.setCommentWriter(writer);
	dto.setCommentTargetWriter(targetWriter);
	dto.setCommentContent(content);
	dto.setCommentParentNum(parentNum);
	dto.setCommentGroupNum(groupNum);
	
	// 만약 원글의 댓글이면
	if(groupNum == 0){
		dto.setCommentGroupNum(num); //원글의 댓글은 자신의 글번호가 댓글의 그룹번호
	}else{
		dto.setCommentGroupNum(groupNum); //대댓은 전송된 그룹번호가 댓글의 그룹번호
	}
	
	// DB 저장
	boolean isSuccess = CommentDao.getInstance().insert(dto);
	
	// 응답 처리
	String cPath = request.getContextPath();
	response.sendRedirect(cPath + "/post/view.jsp?num=" + parentNum);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/post/save-comment.jsp</title>
</head>
<body>

</body>
</html>