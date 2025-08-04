<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/post/form.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
<!-- Toast UI Editor CSS -->
<link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/post/post-form.css">
<!-- Toast UI Editor JS -->
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>

<!-- 한국어 번역 파일 추가 -->
<script src="https://uicdn.toast.com/editor/latest/i18n/ko-kr.js"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp">
		<jsp:param value="board" name="thisPage"/>	
	</jsp:include>
	<div class="container">
		<h3 class="mt-4 mb-4"><i class="bi bi-image me-3"></i>게시글 작성 폼</h3>
		<form action="upload.post" method="post" id="saveForm">
			<div class="mb-2">
				<label class="form-label" for="title">제목</label>
				<input class="form-control" type="text" name="title" id="title" />			
			</div>
			<div class="mb-2">
				<label class="form-label" for="editor">내용</label>
				<!-- Editor ui 가 출력될 div -->
				<div id="editor"></div>
				<textarea class="form-control" name="content" id="hiddenContent" required></textarea>
			</div>
			
			<div class="mb-3">
				<label class="form-label">image upload</label>
					<div class="drop-zone" id="dropZone">
						Drag & Drop Or Click
						<input type="file" name="images" id="fileInput" multiple hidden>
					</div>
					<div class="preview d-flex flex-wrap mt-3" id="preview"></div>
			</div>
			<input type="hidden" name="imageAction" value="post"/>
			<button class="btn btn-success btn-sm" type="submit">save</button>
		</form>
		
	</div>
	
	<jsp:include page="/WEB-INF/include/image-upload-form.jsp" />

<script src="${pageContext.request.contextPath}/js/admin/image-upload-form.js"></script>
	
</body>
</html>