<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// usersId 가져오기
String usersId = (String) session.getAttribute("usersId");
String bookNum = request.getParameter("bookNum");
String stayNum = request.getParameter("stayNum");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/review/review-form.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr"
	crossorigin="anonymous">
    <style>
        .star-rating input[type="radio"] {
            display: none;
        }
        .star-rating label {
            font-size: 2rem; /* 별 크기 */
            color: #ddd; /* 별 기본 회색으로 */
            cursor: pointer;
        }
        .star-rating input[type="radio"]:checked ~ label,
        .star-rating label:hover,
        .star-rating label:hover ~ label {
            color: gold; /* 선택한 별은 금색으로 */
        }
    </style>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
<div class="container mt-4">
    <h3>리뷰 작성</h3>
    
    	<form action="${pageContext.request.contextPath}/review/submit" method="post">

        <!-- 별점 선택 -->
        <div class="mb-3">
            <label class="form-label">별점</label>
            <div class="star-rating d-flex flex-row-reverse justify-content-start">
                <input type="radio" name="rating" value="5" id="star5"><label for="star5">★</label>
                <input type="radio" name="rating" value="4" id="star4"><label for="star4">★</label>
                <input type="radio" name="rating" value="3" id="star3"><label for="star3">★</label>
                <input type="radio" name="rating" value="2" id="star2"><label for="star2">★</label>
                <input type="radio" name="rating" value="1" id="star1"><label for="star1">★</label>
                <!-- 1 ~ 5 제출 된 별의 값이 DB에 들어갈 예정 -->
            </div>
        </div>

        <!-- 리뷰 내용 -->
        <div class="mb-3">
            <label for="comment" class="form-label">리뷰 내용</label>
            <textarea class="form-control" id="comment" name="comment" rows="4" required></textarea>
        </div>

        <!-- 예약번호, usersId, 숙소 번호  -->
        <input type="hidden" name="bookNum" value="<%=bookNum %>">
        <input type="hidden" name="usersId" value="${sessionScope.usersId}">
        <input type="hidden" name="stayNum" value="<%=stayNum %>">

        <button type="submit" class="btn btn-primary">리뷰 등록</button>
    </form>
</div>
</body>
</html>