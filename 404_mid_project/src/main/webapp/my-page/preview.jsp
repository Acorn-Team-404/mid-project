<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.book.BookDao" %>
<%@ page import="model.book.BookDto" %>

<%
	//유저 번호 세션 가져오기
    Long userNum = (Long) session.getAttribute("usersNum");
	// 세션이 없으면 로그인폼으로 이동
    if (userNum == null) {
        response.sendRedirect(request.getContextPath() + "/user/login-form.jsp");
        return;
    }
 	// 예약 목록 가져오기
    BookDao dao = BookDao.getInstance();
    List<BookDto> bookings = dao.getByUserNum(userNum);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>/my-page/preview.jsp</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<div class="container">
    <h2 class="mb-4 text-center">예약 내역</h2>

    <% if (bookings != null && !bookings.isEmpty()) { %>
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-dark text-center">
                    <tr>
                        <th>예약번호</th>
                        <th>체크인</th>
                        <th>체크아웃</th>
                        <th>상세</th>
                    </tr>
                </thead>
                <tbody class="text-center">
                    <% for (BookDto dto : bookings) { %>
                        <tr>
                            <td><%= dto.getBookNum() %></td>
                            <td><%= dto.getBookCheckIn() %></td>
                            <td><%= dto.getBookCheckOut() %></td>
                            <td>
                                <a href="<%= request.getContextPath() %>/mypage/detail?bookNum=<%= dto.getBookNum() %>" class="btn btn-sm btn-primary">보기</a>
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

</body>
	<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
</html>
