<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>/booking/book-confirm.jsp</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
	<div class="container my-5">
	    <!-- 전체 박스 -->
	    <div class="border rounded-3 bg-white p-4">
	
	        <!-- 상단 제목 -->
	        <h2 class="text-center fw-bold mb-4">예약 내역</h2>
	
	        <!-- 숙소 정보 -->
	        <div class="mb-3">
	            <h5 class="fw-bold">숙소명</h5>
	            <div>${stay.stayName}</div>
	            <div>숙소 위치</div>
	            <div>${stay.stayAddr}</div>
	            <div>숙소 전화번호</div>
	            <div>${stay.stayPhone}</div>
	        </div>
	
	        <!-- 결제 날짜 -->
	        <div class="text-end text-muted mb-3" style="font-size: 0.85rem;">
			    결제 날짜
			    <div style="font-size: 0.85rem;">
			        ${paidAtDate}<br/>
			        ${paidAtTime}
			    </div>
			</div>
	
	        <!-- 예약 번호 + 체크인/체크아웃 -->
	        <div class="mb-4">
	            <h6 class="fw-bold">예약 번호</h6>
	            <div>${book.bookNum}</div>
	            <hr>
	            <div class="row">
	                <div class="col-md-6">
	                    <h6 class="fw-bold">체크인</h6>
	                    <div>날짜</div>
	                    <div>${checkInFormat}</div>
	                    <div>시간</div>
	                    <div id="checkInTime">${book.bookCheckInTime}</div>
	                </div>
	                <div class="col-md-6">
	                    <h6 class="fw-bold">체크아웃</h6>
	                    <div>날짜</div>
	                    <div>${checkOutFormat}</div>
	                    <div>시간</div>
	                    <div>오전 11시</div>
	                </div>
	            </div>
	        </div>
	
	        <hr>
	
	        <!-- 고객 정보 -->
	        <div class="mb-4">
	            <h6 class="fw-bold">고객 이름 : ${users.usersName}</h6>
	
	            <h6 class="fw-bold mt-3">투숙인원</h6>
	            <div>성인 : ${book.bookAdult}명</div>
	            <div>어린이 : ${book.bookChildren}명</div>
	            <div>유아 : ${book.bookInfant}명</div>
	
	            <h6 class="fw-bold mt-3">예약 객실명 : ${room.roomName}</h6>
	            <h6 class="fw-bold mt-3">요청사항</h6>
	            <div>${book.bookRequest}</div>
	        </div>
	
	        <!-- 결제 요금 -->
	        <div class="mb-4">
	            <h6 class="fw-bold">결제 요금 : ${payment.payAmount}원</h6>
	        </div>
	
	        <!-- 취소 규정 -->
	        <div class="text-center my-4">
			    <h5 class="fw-bold">취소 규정</h5>
			    <ul class="list-group list-group-flush text-start">
			        <li class="list-group-item">· 예약 취소 시, 체크인 7일 전까지 전액 환불이 가능합니다.</li>
			        <li class="list-group-item">· 체크인 3일 전부터 7일 전까지는 50% 환불이 가능합니다.</li>
			        <li class="list-group-item">· 체크인 3일 이내 취소 및 노쇼(No-show) 시 환불이 불가합니다.</li>
			        <li class="list-group-item">· 취소는 반드시 고객센터를 통해 접수해주시기 바랍니다.</li>
			    </ul>
			</div>
	
	        <hr>
	
	        <!-- 이용 약관 -->
	        <div class="mb-3">
			    <div class="fw-bold mb-2">이용 약관</div>
			    <p>
			        1. 객실 내에서는 금연이며, 흡연 시 청소비가 부과될 수 있습니다.<br>
			        2. 반려동물 출입은 제한되며, 별도 문의 바랍니다.<br>
			        3. 예약 시 입력한 인원 외 추가 인원 입실은 불가합니다.<br>
			        4. 시설물 파손 시 손해배상 청구가 있을 수 있습니다.<br>
			        5. 체크인 시간은 오후 3시부터이며, 체크아웃 시간은 오전 11시까지입니다.<br>
			        6. 본 약관은 관계 법령 및 숙소 규정에 따릅니다.
			    </p>
			</div>
	    </div>
	</div>
	<script>
		const cTime = document.querySelector("#checkInTime");
		const cTimeMap = {
			early : "오전 10시 - 12시",
			standard : "오후 3시",
			late : "오후 5시 - 11시"			
		}
		cTime.textContent = cTimeMap[cTime.textContent];
	</script>
</body>
</html>