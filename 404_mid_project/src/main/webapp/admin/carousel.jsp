<%@page import="controller.tester.ImageDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
    // request 영역에 저장된 imageList 꺼내오기
    List<ImageDto> imageList = (List<ImageDto>) request.getAttribute("imageList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>NAS 이미지 캐러셀</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .carousel-inner img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      object-position: center;
    }
  </style>
</head>
<body>

<h3>NAS에 저장된 이미지 출력 (인디케이터 포함)</h3>

<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">

  <!-- 🔘 인디케이터 (동적 생성) -->
  <div class="carousel-indicators">
    <%
      for (int i = 0; i < imageList.size(); i++) {
        String activeClass = (i == 0) ? "class=\"active\"" : "";
    %>
      <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="<%=i%>" <%=activeClass%> aria-label="Slide <%=i+1%>"></button>
    <%
      }
    %>
  </div>

  <!-- 🖼️ 이미지 영역 -->
  <div class="carousel-inner ratio ratio-16x9">
    <%
      for (int i = 0; i < imageList.size(); i++) {
        ImageDto dto = imageList.get(i);
        String fileName = dto.getFileName();
        String activeClass = (i == 0) ? "active" : "";
    %>
      <div class="carousel-item <%=activeClass%>">
        <img src="<%=request.getContextPath()%>/showImage?name=<%=fileName%>" class="d-block w-100" alt="이미지 <%=i+1%>">
      </div>
    <%
      }
    %>
  </div>

  <!-- ◀️ ▶️ 좌우 버튼 -->
  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
