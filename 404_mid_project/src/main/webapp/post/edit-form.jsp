<%@page import="model.post.PostDao"%>
<%@page import="model.post.PostDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	int num=Integer.parseInt(request.getParameter("num"));
	PostDto dto=PostDao.getInstance().getByPostNum(num);
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/post/edit-form.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp">
		<jsp:param value="edit-form" name="thisPage"/>
	</jsp:include>
	
	<h1>게시글 수정</h1>
		<form action="update.post" method="post" id="edit-form">
			<div>
				<label class="form-lable" for="num">글 번호</label>
				<input class="form-control" type="text" name="num" id="num" value="<%=dto.getPostNum() %>" readonly />
			</div>
			<div>
				<label class="form-lable" for="writer">작성자</label>
				<input class="form-control" type="text" name="writer" id="writer" value="<%=dto.getPostWriterId() %>" readonly />
			</div>
			<div>
				<label class="form-lable" for="title">제목</label>
				<input class="form-control" type="text" name="title" id="title" value="<%=dto.getPostTitle() %>" />
			</div>
			<div>
				<label class="form-lable" for="editor">내용</label>
				
				<div id="editor"></div>
				
				<textarea class="form-control" name="content" id="hiddenContent" ><%=dto.getPostContent() %></textarea>
			</div>
			<button class="btn btn-success btn-sm" type="submit">Edit</button>
		</form>
	
	</div>
	
	<script>
		// 위에 toast ui javascript 가 로딩되어있으면 toastui.Editor 클래스 사용 가능
		// 해당 클래스 이용해서 객체 생성하면서 {} object 로 ui 관련된 옵션 잘 전달하면
		// 우리가 원하는 모양의 텍스트 편집기를 만들 수 있다.
		const editor = new toastui.Editor({
			el: document.querySelector(' #editor'),
			height: '500px',
			initialEditType: 'wysiwyg',
			previewStyle: 'vertical',
			language: 'ko',
			initialValue: `<%=dto.getPostContent() %>`
		});
		
		document.querySelector("#editForm").addEventListener("submit", (e)=>{
			// 에디터로 작성된 문자열 읽어오기
			const content = editor.getHTML();
			// 테스트로 콘솔에 출력하기
			console.log(content);
			//에디터로 작성된 문자열을 폼 전송이 될 수 있도록 textarea의 value 값으로 넣어준다
			document.querySelector("#hiddenContent").value=content;
			// 테스트하기 위해 폼 전송 막기, 막지 않으면->전송
			//e.preventDefault();
		})
	</script>
	
	
</body>
</html>