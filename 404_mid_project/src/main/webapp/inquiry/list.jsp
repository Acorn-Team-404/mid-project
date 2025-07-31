<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/inquiry/list.jsp</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<div class="text-center mt-5">
			<h1>1:1 문의 내역</h1>
			<a class="btn btn-outline-primary btn-sm mt-5" href="new-inquiry.jsp">
				문의하기
			</a>					
		</div>
		<div class="mt-4 mb-3">
			<form action="/inquiry/search" method="get" class="row gy-2 gx-3 align-items-center">
				<div class="col-4  d-flex">
			    	<label class="form-label d-none" for="startDate">시작일</label>
			    	<input type="date" class="form-control form-control-sm me-2" id="startDate" name="startDate">
			    	<label class="form-label d-none" for="endDate">종료일</label>
			    	<input type="date" class="form-control form-control-sm" id="endDate" name="endDate">
				</div>
			
				<div class="col-auto">
					<button type="submit" class="btn btn-sm btn-primary">검색</button>
				</div>
			</form>
		</div>
		<div class="p-1 mw-100 border-2 rounded-3">
            <div class="d-flex align-items-center row text-center p-3">
                <span class="col-1">번호</span>
                <span class="col-2">유형</span>
                <span class="col">제목</span>
                <span class="col-2">문의일</span>
                <span class="col-1">상태</span>
            </div>
            <ul class="list-unstyled p-0 m-0 rounded shadow-sm overflow-hidden">
                <li class="d-flex align-items-center text-center p-3" onclick="toggleDetails(this)">
                    <span class="col-1">001</span>
                    <span class="col-2">배송</span>
                    <span class="col">배송 관련 문의입니다</span>
                    <span class="col-2">2025-07-30</span>
                    <span class="col-1">
                        <span class="badge bg-success">답변완료</span>
                        <span class="toggle-icon">▼</span>
                    </span>
                </li>
                <div class="p-3 d-none">
                    <p><strong>문의 내용</strong></p>
                    <p>답변</p>
                    <p>답변</p>
                </div>
            </ul>
		</div>		
	</div>
	<script>
		function toggleDetails(e) {
	        const details = e.nextElementSibling;
	        const isActive = e.classList.contains('active');
	        
	        if (isActive) {
	            e.classList.remove('active');
	            details.classList.add('d-none');
	        } else {
	            e.classList.add('active');
	            details.classList.remove('d-none');
	        }
	    }
	</script>
</body>
</html>