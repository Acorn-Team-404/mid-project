<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Date"%>
<%@page import="model.pay.PaymentDao"%>
<%@page import="model.user.UserDto"%>
<%@page import="model.user.UserDao"%>
<%@page import="model.page.StayDao"%>
<%@page import="java.util.List"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.room.RoomDao"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.pay.PaymentDto"%>
<%@page import="model.book.BookDto"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  	// PaymentsServlet에서 트랜잭션 성공 여부로 결과페이지 분기
  	Boolean success =(Boolean) request.getAttribute("paymentSuccess");
  	if (success == null) success = false;
  	// PaymentServlet에서 포워딩한 예약정보 + 결제 정보
  	BookDto bookDto = (BookDto) request.getAttribute("bookDto"); 
  	PaymentDto paymentDto = (PaymentDto) request.getAttribute("paymentDto");
  	String pay_method_code = PaymentDao.getInstance().getMethodCodeByPayNum(paymentDto.getPayNum());
  	//예약db에 있는 숙소 번호로 예약한 숙소정보 가져오기
  	StayDto stayDto = StayDao.getInstance().getByBookStayNum(bookDto.getBookStayNum());
  	RoomDto roomDto = RoomDao.getInstance().getByNum(bookDto.getBookRoomNum());
  	UserDto usersDto = UserDao.getInstance().getByUserNum(bookDto.getBookUsersNum());
	
  	//에러 응답코드 
  	String errorCode = (String) request.getAttribute("errorCode");
  	String errorMsg = (String) request.getAttribute("errorMsg");
  	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
  	String checkInDateOnly = bookDto.getBookCheckIn().substring(0, 10);
	String checkOutDateOnly = bookDto.getBookCheckOut().substring(0, 10);
	Date checkInDate = Date.valueOf(checkInDateOnly);
	Date checkOutDate = Date.valueOf(checkOutDateOnly);
	String checkInFormat = dateFormat.format(checkInDate);
	String checkOutFormat = dateFormat.format(checkOutDate);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>결제 확인</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background-color: #f8f9fa;
      font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif;
    }
    .container {
      max-width: 720px;
      margin-top: 80px;
    }
    .card {
      border: none;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      padding: 40px;
      text-align: center;
    }
    .card h2 {
      font-size: 1.8rem;
      font-weight: 600;
      margin-bottom: 30px;
    }
    .info {
      font-size: 1.1rem;
      margin-bottom: 10px;
    }
    .btn-dark {
      padding: 12px 0;
      font-size: 1rem;
      border-radius: 8px;
    }
    /* 얇은 선 박스 스타일 */
    .border-box {
      border: 1px solid #ddd;
      border-radius: 6px;
      padding: 12px 20px;
      margin-bottom: 20px;
      text-align: left;
      background-color: #fff;
    }
    .border-box p {
      margin: 6px 0;
      font-weight: 500;
      color: #333;
    }
    .border-box span {
      font-weight: normal;
      margin-left: 8px;
      color: #555;
    }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-12 col-md-10 col-lg-8">
      <div class="card p-4 shadow-sm">

        <% if (success) { %>
          <h2 class="mb-4 text-center">결제가 완료되었습니다.</h2>

          <p><strong>예약자 성함:</strong> <span><%= usersDto.getUsersName() %></span></p>

          <div class="border-box mb-3">
            <p><strong>체크인:</strong> <span><%= checkInFormat %></span></p>
            <p><strong>체크아웃:</strong> <span><%= checkOutFormat %></span></p>
          </div>

          <div class="row">
            <div class="col-md-6 mb-2">
              <strong>숙소명:</strong> <span><%= stayDto.getStayName() %></span>
            </div>
            <div class="col-md-6 mb-2">
              <strong>객실명:</strong> <span><%= roomDto.getRoomName() %></span>
            </div>
          </div>

          <div class="row">
            <div class="col-4"><strong>성인:</strong> <%= bookDto.getBookAdult() %></div>
            <div class="col-4"><strong>아동:</strong> <%= bookDto.getBookChildren() %></div>
            <div class="col-4"><strong>영유아:</strong> <%= bookDto.getBookInfant() %></div>
          </div>

          <div class="row mt-2">
            <div class="col-md-6"><strong>총 인원:</strong> <%= bookDto.getBookTotalPax() %></div>
            <div class="col-md-6"><strong>추가 침대:</strong> <%= bookDto.getBookExtraBed() %> / 유아침대: <%= bookDto.getBookInfantBed() %></div>
          </div>

          <div class="border-box my-3">
            <p><strong>고객 요청사항:</strong></p>
            <p><%= bookDto.getBookRequest() %></p>
          </div>

          <p><strong>결제 수단:</strong> <%= pay_method_code %></p>
          <p><strong>최종 결제 금액:</strong> <%= bookDto.getBookTotalAmount() %>원</p>

          <div class="row mt-4">
            <div class="col-6">
              <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-dark w-100">홈으로</a>
            </div>
            <div class="col-6">
              <a href="${pageContext.request.contextPath}/my-page/preview.jsp" class="btn btn-dark w-100">예약내역</a>
            </div>
          </div>

        <% } else { %>
          <h2 class="text-danger text-center mb-4">결제를 실패했습니다</h2>

          <div class="border-box mb-3">
            <p><strong>오류 코드:</strong> <%= errorCode != null ? errorCode : "알 수 없음" %></p>
            <p><strong>오류 메시지:</strong> <%= errorMsg != null ? errorMsg : "일시적인 오류가 발생했습니다." %></p>
          </div>

          <div class="row">
            <div class="col-6">
              <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-dark w-100">홈으로</a>
            </div>
            <div class="col-6">
              <a href="${pageContext.request.contextPath}/booking/booking-page.jsp" class="btn btn-dark w-100">다시 결제</a>
            </div>
          </div>
        <% } %>

      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
</body>
