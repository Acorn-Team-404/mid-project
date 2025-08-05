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
			
			<button class="btn btn-success btn-sm" type="submit">save</button>
		</form>
		
	</div>
	
	<script>
	const dropZone = document.querySelector("#dropZone");
    const fileInput = document.querySelector("#fileInput");
    const preview = document.querySelector("#preview");
    
    let selectedFiles = [];
    
    // dropzone click=> input type="file" 을 강제 클릭
    dropZone.addEventListener("click", ()=>fileInput.click());
    
    // drag
    dropZone.addEventListener("dragover", (e)=>{
    	e.preventDefault();
    	// dragover->회색
    	dropZone.classList.add("dragover")
    });
    
    // drag 나갔을 때
    dropZone.addEventListener("dragleave", () => {
    	// dragover 클래스 제거하여 원래 색상으로 돌아가도록
        dropZone.classList.remove("dragover");
    });
    
    // 파일 drag -> drop
    dropZone.addEventListener("drop", (e) => {
    	e.preventDefault();
    	dropZone.classList.remove("dragover");
    	// 선택된 파일객체 배열 -> 기능 모두 가능한 배열로 변경
    	const files = Array.from(e.dataTransfer.files);
    	// 기존 배열에 추가
    	selectedFiles = selectedFiles.concat(files);
    	
    	updatePreview();
    })
    
    // 파일 직접 선택 input type="file"에 change 이벤트
    fileInput.addEventListener("change", ()=>{
    	const files = Array.from(fileInput.files);
        selectedFiles = selectedFiles.concat(files);
        updatePreview();
    })
    
    // preview 업데이트 ( await 비동기 함수 -> async 키워드 붙여야 함 )
    async function updatePreview(){
    	preview.innerHTML="";
    	// input type="file"에 value 넣기
    	const dataTransfer=new DataTransfer();
    	for(let i=0; i<selectedFiles.length; i++){
    		const file = selectedFiles[i];
    		if(!file.type.startsWith("image/")) continue; //continue:건너뛰기
    		
    		try{
    			const imageUrl = await readFileAsDataURL(file);
    			
    			const container = document.createElement("div");
    			container.classList.add("preview-item");
    			
    			const img = document.createElement("img");
    			img.setAttribute("src", imageUrl);
    			
    			const btn = document.createElement("button");
    			btn.classList.add("remove-btn");
    			btn.innerText="x"
    			
    			btn.addEventListener("click", ()=>{
    				// selectedFiles 배열에서 i번째 방으로부터 1개의 item 삭제
    				selectedFiles.splice(i,1);
    				updatePreview();
    			});
    			
    			container.insertAdjacentElement("beforeend", img);
                container.insertAdjacentElement("beforeend", btn);
                preview.insertAdjacentElement("beforeend", container);
                
                // DataTransfer 에 파일추가
                dataTransfer.items.add(file);
                
    		} catch (err){
    			console.error("이미지 로딩 실패", err);
    		}
    	}
    	
    	// 선택한 파일 배열을 input type="file"의 value로 넣기
    	fileInput.files = dataTransfer.files;
    	
    	}
    
    	// Promise 객체 리턴
    	function readFileAsDataURL(file){
    		return new Promise((resolve, reject)=>{
    			const reader = new FileReader();
    			reader.onload = () => resolve(reader.result);
    			reader.onerror = reject;
    			reader.readAsDataURL(file);
    		});
    	}
    	
    	// 폼에 "submit" 이벤트 일어났을 때
    	document.querySelector("#saveForm").addEventListener("submit", async(e)=>{
    		e.preventDefault();
    		if(selectedFiles.length < 1){
    			alert("업로드할 이미지를 1개 이상 선택해주세요.");
    			return;
    		}
    		
    	
    	});
    
	</script>
	
</body>
</html>