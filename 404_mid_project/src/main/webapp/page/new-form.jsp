<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<String> imageList = new ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/new-form</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>

<!-- Toast UI Editor CSS -->
<link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
<!-- Toast UI Editor JS -->
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<!-- 한국어 번역 파일 추가 -->
<script src="https://uicdn.toast.com/editor/latest/i18n/ko-kr.js"></script>
</head>
<body>
	<form action="save.jsp" method="post" id="saveForm">
		<div>
			<h1>슬라이드(Carousel) 이미지 첨부</h1>
			  <input type="file" name="imageFiles" multiple accept="image/*">
		</div>
		<div class="Container" id="info">
			<h1>소개글 작성</h1>
			<div class="mb-2">
				<!-- Editor ui 이 출력될 div -->
				<div id="editor"></div>
			</div>
		</div>
		<div class="Container" id="review" hidden>
			<h1>리뷰 출력</h1>
		</div>
		<div class="Container" id="loc" hidden>
			<h1>스테이 지도 출력</h1>
		</div>
		<div class="Container" id="notice">
			<h3>예약 안내</h3>
			<input type="text" name="notice_reserve" id="notice_reserve" class="form-control" />
			
			<h3>이용 안내</h3>
			<input type="text" name="notice_guide" id="notice_guide" class="form-control" />
			
			<h3>환불 규정</h3>
			<input type="text" name="notice_refund" id="notice_refund" class="form-control" />
		</div>
		<button class="btn btn-success btn-sm" type="submit">저장</button>
	</form>
	<script>
		const editor = new toastui.Editor({
			el: document.querySelector('#editor'),
			height: '500px',
			initialEditType: 'wysiwyg',
			previewStyle: 'vertical',
			language: 'ko'
		});
			
		document.querySelector("#saveForm").addEventListener("submit", (e)=>{
			// 에디터로 작성된 문자열 읽어오기
			const content = editor.getHTML();
			// 테스트로 콘솔에 출력하기
			console.log(content);
			// 에디터로 작성된 문자열을 폼 전송이 될 수 있도록 textarea 의 value 로 넣어준다
			document.querySelector("#hiddenContent").value=content;
			
			// 테스트 하기 위해 폼 전송 막기
			e.preventDefault();
			
			// 폼 전송을 막지 않으면 전송이 된다
		});
	</script>
</body>
</html>