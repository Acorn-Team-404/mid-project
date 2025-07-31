<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/booking/room-info.jsp</title>
</head>
<body>
<div class="container mt-5">
	<form action="${pageContext.request.contextPath }/booking/submit" method="post">
    <!-- 객실 카드 시작 -->
    <div class="room-card row g-0 mb-4">
        <!-- 이미지 영역 -->
        <div class="col-md-5">
            <img src="/images/room-sample.jpg" class="img-fluid room-image" alt="객실 이미지">
        </div>

        <!-- 정보 영역 -->
        <div class="col-md-7 p-4 d-flex flex-column justify-content-between">
            <div>
                <h4 class="fw-bold"> 숙소명 </h4>
                <p class="text-muted mb-1"> 객실 타입: 기본형 / 기준 6명 (최대 12명) </p>
                <p class="mb-2"><strong>객실 설명:</strong> 햇살이 잘 들어오는 모던한 스탠다드 객실입니다.</p>
                <p class="mb-2"><strong>편의시설:</strong> 와이파이, 에어컨, TV, 전자레인지 </p>                
            </div>

            <div class="d-flex justify-content-between align-items-center mt-3">
                <div>
                    <div class="price-original"> 금액 ₩300,000</div>
                </div>
                <button class="btn btn-room-select">객실 선택</button>
            </div>
        </div>
    </div>
    <!-- 객실 카드 끝 -->
	</form>
</div>
</body>
</html>