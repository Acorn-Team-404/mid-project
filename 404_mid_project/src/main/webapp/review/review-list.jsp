<%@page import="model.review.ReviewDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/review/review-list.jsp</title>
  <style>
        .star { color: gold; font-size: 1.2rem; }
        .star.gray { color: lightgray; font-size: 1.2rem; }
        .review-box {
            border: 1px solid #ccc;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 8px;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h3>리뷰 목록</h3>
<%
    List<ReviewDto> reviews = (List<ReviewDto>) request.getAttribute("reviews");
	System.out.println("가져온 리뷰 수: " + reviews.size());
    if (reviews == null || reviews.isEmpty()) {
%>
    <p>등록된 리뷰가 없습니다.</p>
<%
    } else {
        for (ReviewDto r : reviews) {
%>
        <div class="review-box">
            <strong><%= r.getUsersId() %></strong> | <%= r.getCreatedAt() %><br/>
            <div>
<%
                for (int i = 1; i <= 5; i++) {
                    if (i <= r.getRating()) {
%>                  <span class="star">★</span>
<%
                    } else {
%>                  <span class="star gray">★</span>
<%
                    }
                }
%>
            </div>
            <p><%= r.getComment() %></p>
        </div>
<%
        }
    }
%>
</div>
</body>
</html>