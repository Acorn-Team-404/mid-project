<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>/booking/book-confirm.jsp</title>
</head>
<body>
<div class="container my-5">
    <div class="border rounded p-4">
        <h1 class="text-center fw-bold mb-4">예약 내역</h1>

        <!-- 숙소 정보 -->
        <div class="mb-4">
            <h4 class="fw-bold">숙소명</h4>
            <div>숙소 위치</div>
            <div>숙소 전화번호</div>
        </div>

        <!-- 결제일 -->
        <div class="text-end text-muted mb-3">
            결제 날짜
        </div>

        <!-- 예약 번호 및 체크인/체크아웃 -->
        <div class="mb-4">
            <h5 class="fw-bold">예약 번호</h5>
            <hr>
            <div class="row">
                <div class="col-md-6">
                    <h6 class="fw-bold">체크인</h6>
                    <div>날짜</div>
                    <div>시간</div>
                </div>
                <div class="col-md-6">
                    <h6 class="fw-bold">체크아웃</h6>
                    <div>날짜</div>
                    <div>시간</div>
                </div>
            </div>
        </div>

        <!-- 고객 정보 -->
        <div class="mb-4">
            <h6 class="fw-bold">고객 이름</h6>
            <h6 class="fw-bold mt-3">투숙 인원</h6>
            <ul class="list-unstyled">
                <li>성인: </li>
                <li>어린이: </li>
                <li>유아: </li>
            </ul>
            <h6 class="fw-bold">예약 객실 타입</h6>
            <h6 class="fw-bold">요청사항</h6>
            <div>요청 내용</div>
        </div>

        <!-- 결제 정보 -->
        <div class="mb-4">
            <h6 class="fw-bold">결제 요금</h6>
            <div>₩</div>
        </div>

        <!-- 취소 규정 -->
        <div class="mb-4">
            <h5 class="fw-bold">취소 규정</h5>
            <div>ㅇㅇ 숙소 취소 규정</div>
        </div>

        <!-- 이용 약관 -->
        <div>
            <h6 class="fw-bold">이용 약관</h6>
            <div>체크인은 오후 4시, 체크아웃은 오전 11시입니다. <br>
                최대 인원을 초과하는 인원은 입실이 불가합니다. <br>
                예약 인원 외 방문객의 출입을 엄격히 제한합니다.
            </div>
        </div>
    </div>
</div>
<!-- 예약내역

STAY 테이블
STAY_NAME : 숙소명 
STAY_ADDR : 숙소 위치
STAY_PHONE : 숙소 전화번호

PAYMENT 테이블
PAY_PAID_AT : 결제 날짜
PAY_AMOUNT : 결제 금액

BOOK 테이블
BOOKNUM : 숙소 번호
BOOK_CHECKIN_DATE : 체크인 날짜
BOOK_CHECKOUT_DATE : 체크아웃 날짜
BOOK_CHECKIN_TIME : 체크인 시간 -> 이른 체크인 : 11시, 정규 시간 : 2시, 늦은 도착 : 3시
체크아웃 시간은 무조건 12시로 설정
BOOK_USERS_NAME :  고객 이름
BOOK_ADULT : 성인
BOOK_CHILDREN : 어린이
BOOK_INFANT :  유아
BOOK_REQUEST : 요청사항


ROOM 테이블
예약 객실 타입 :  ROOM_NAME -->
</body>
</html>