<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Date"%>
<%@page import="model.review.ReviewDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.page.StayDao"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.room.RoomDao"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.book.BookDao" %>
<%@ page import="model.book.BookDto" %>

<%
    Long userNum = (Long) session.getAttribute("usersNum");
    if (userNum == null) {
        response.sendRedirect(request.getContextPath() + "/user/login-form.jsp");
        return;
    }
	
 	 BookDao dao = BookDao.getInstance();
 	 List<BookDto> bookings = dao.getByUserNum(userNum);
 	 
 	 
  
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>/my-page/preview.jsp</title>
    <jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<div class="container">
    <h2 class="mb-4 text-center">예약 내역</h2>

    <% if (bookings != null && !bookings.isEmpty()) { %>
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle">
                <thead class="table-dark text-center">
                    <tr>
                        <th>예약번호</th>
                        <th>숙소 이름</th>
                        <th>체크인</th>
                        <th>체크아웃</th>
                        <th>예약상태</th>
                        <th>상세</th>
                        <th>리뷰</th>
                    </tr>
                </thead>
                <tbody class="text-center">
                    <% for (BookDto dto : bookings) {
                        String bookNum = dto.getBookNum();
                        int statusCode = dto.getBookStatusCode();
                        long stayNum = dto.getBookStayNum();
                        

                        // 숙소 이름 가져오기
                        String stayName = "이름 없음"; // 만약 숙소 이름 못 가져오면 임시로
                        StayDto stayDto = StayDao.getInstance().getByBookStayNum(stayNum);
                        // db에 정보가 있으면
                        if (stayDto != null) {
                            stayName = stayDto.getStayName();
                        }
                        // 예약 상태 글자로 나오게 하기
                        String statusName = ReviewDao.getInstance().getReservationStatusName(statusCode);
                    %>
                    <tr>
                        <td><%= bookNum %></td>
                        <td><%= stayName %></td>
                        <td><%= dto.getBookCheckIn().substring(0, 10) %></td> <!-- substring() 써서 00:00:00 출력 안 하게 함 -->
						<td><%= dto.getBookCheckOut().substring(0, 10) %></td> <!-- substring() 써서 00:00:00 출력 안 하게 함 -->
                        <td><%= statusName %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/booking/confirm?bookNum=<%= bookNum %>" class="btn btn-sm btn-primary">보기</a>
                        </td>
                        <td>
                        		<!-- 만약 예약코드가 11이면  -->
                            <% if (statusCode == 11) {
                                boolean hasReview = ReviewDao.getInstance().hasReview(bookNum);
                                if (hasReview) { %> <%--예약 번호로 리뷰 쓴 기록이 있다면 리뷰를 썼으면 리뷰 보기로 나오고--%>
                                    <a href="<%= request.getContextPath() %>/review/list?stayNum=<%= stayNum %>" class="btn btn-sm btn-light">리뷰 보기</a>
                            <%  } else { %><!-- 리뷰를 안 썼으면 리뷰 쓰기로 나오고 -->
                                    <a href="<%= request.getContextPath() %>/review/review-form.jsp?bookNum=<%= bookNum %>&stayNum=<%= stayNum %>" class="btn btn-sm btn-secondary">리뷰 쓰기</a>
                            <%  }
                               } else { %> <!-- 예약코드가 10이면 -로 나오게 -->
                                <span class="text-muted">-</span>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } else { %>
        <div class="alert alert-warning text-center">예약 내역이 없습니다.</div>
    <% } %>
</div>

<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
</body>
</html>
