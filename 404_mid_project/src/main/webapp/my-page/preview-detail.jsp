<%@page import="model.book.BookDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    BookDto dto = (BookDto) request.getAttribute("booking");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>예약 상세 보기</title>
    <!-- Bootstrap CDN 추가 -->
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body class="bg-light">

<div class="container my-5">
    <h2 class="mb-4">📋 예약 상세 보기</h2>

    <% if (dto != null) { %>
        <div class="card shadow-sm">
            <div class="card-body">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><strong>예약 날짜</strong>: <%= dto.getBookCreatedAt() %></li>
                    <li class="list-group-item"><strong>예약 번호</strong>: <%= dto.getBookNum() %></li>
                    <li class="list-group-item"><strong>숙소 번호</strong>: <%= dto.getBookStayNum() %></li>
                    <li class="list-group-item"><strong>추가 요청 사항</strong>: <%= dto.getBookRequest() %></li>
                    <li class="list-group-item"><strong>총 인원 수</strong>: <%= dto.getBookTotalPax() %> 명</li>
                    <li class="list-group-item"><strong>성인</strong>: <%= dto.getBookAdult() %> 명 / 
						<strong>어린이</strong>: <%= dto.getBookChildren() %> 명 / <strong>유아</strong>: <%= dto.getBookInfant() %> 명</li>
                    <li class="list-group-item"><strong>체크인</strong>: <%= dto.getBookCheckIn() %></li>
                    <li class="list-group-item"><strong>체크아웃</strong>: <%= dto.getBookCheckOut() %></li>
                    <li class="list-group-item"><strong>총 결제 금액</strong>: <%= dto.getBookTotalAmount() %> 원</li>
                </ul>
                <div class="mt-4 text-end">
                    <a href="<%= request.getContextPath() %>/my-page/preview.jsp" class="btn btn-outline-secondary">← 돌아가기</a>
                </div>
            </div>
        </div>
    <% } else { %>
        <div class="alert alert-danger" role="alert">
            예약 정보를 불러올 수 없습니다.
        </div>
    <% } %>
</div>

</body>
</html>
