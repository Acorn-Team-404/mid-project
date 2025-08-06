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
  
  String errorCode = (String) request.getAttribute("errorCode");
  String errorMsg = (String) request.getAttribute("errorMsg");
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

<div class="container">
<% if (success) { %>
  <div class="card">
    <h2>결제가 완료되었습니다.</h2>
    
    <p><strong>예약자 성함 :</strong> <span><%=usersDto.getUsersName()%></span></p>

    <div class="border-box">
      <p><strong>체크인 시간 :</strong> <span><%= bookDto.getBookCheckIn() %></span></p>
      <p><strong>체크아웃 시간 :</strong> <span><%= bookDto.getBookCheckOut() %></span></p>
    </div>

    <p><strong>숙소명 :</strong> <span><%=stayDto.getStayName() %></span></p>
    <p><strong>객실명 :</strong> <span></span><%=roomDto.getRoomName() %></p>
    <p><strong>성인:</strong> <span><%=bookDto.getBookAdult()%></span></p>
    <p><strong>아동 인원:</strong> <span><%=bookDto.getBookChildren()%></span></p>
    <p><strong>영유아:</strong> <span><%=bookDto.getBookInfant()%></span></p>
    <p><strong>총 인원 :</strong> <span><%= bookDto.getBookTotalPax() %></span></p>
    <p><strong>추가 간이 침대 :</strong> <span><%= bookDto.getBookExtraBed() %></span></p>
    <p><strong>추가 유아 침대:</strong> <span><%=bookDto.getBookInfantBed()%></span></p>

    <div class="border-box">
      <p><strong>고객 요청사항 :</strong></p>
      <p><span><%= bookDto.getBookRequest() %></span></p>
    </div>
    
	<p><strong>결제 수단 :</strong> <span><%= pay_method_code %></span></p>
    <p><strong>최종 결제 금액 :</strong> <span><%= bookDto.getBookTotalAmount() %></span></p>

    <!-- 버튼 영역 -->
    <div class="row mt-4">
      <div class="col-6">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-dark w-100">홈으로</a>
      </div>
      <div class="col-6">
        <a href="${pageContext.request.contextPath}/my-page/preview.jsp" class="btn btn-dark w-100">예약내역 보기</a>
      </div>
    </div>
  </div>
<% } else { %>
  <div class="card">
    <h2 style="color: #dc3545;">결제를 실패했습니다</h2>

    <div class="border-box">
      <p><strong>오류 코드:</strong> <span><%= errorCode != null ? errorCode : "알 수 없음" %></span></p>
      <p><strong>오류 메시지:</strong> <span><%= errorMsg != null ? errorMsg : "일시적인 오류가 발생했습니다. 다시 시도해주세요." %></span></p>
    </div>

    <!-- 버튼 영역 -->
    <div class="row mt-4">
      <div class="col-6">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-dark w-100">홈으로</a>
      </div>
      <div class="col-6">
        <a href="${pageContext.request.contextPath}/booking/booking-page.jsp" class="btn btn-dark w-100">다시 결제하기</a>
      </div>
    </div>
  </div>
<% } %>
</body>
</html>
