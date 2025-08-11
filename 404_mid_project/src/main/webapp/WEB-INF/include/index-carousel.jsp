<%@page import="model.admin.IndexDao"%>
<%@page import="model.image.ImageDao"%>
<%@page import="model.image.ImageDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
		int carouselNum = IndexDao.getInstance().getIndexCarouselNum();
    List<ImageDto> carouselImages = ImageDao.getInstance().getListByTarget("index", carouselNum); // 예: index 영역
    int size = carouselImages.size();
%>
	<div class="mb-3">
		<% if (size > 0) { %>
		<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
		  <div class="carousel-indicators">
		    <% for (int i = 0; i < size; i++) { %>
		      <button type="button"
		              data-bs-target="#carouselExampleIndicators"
		              data-bs-slide-to="<%= i %>"
		              class="<%= (i == 0) ? "active" : "" %>"
		              aria-current="<%= (i == 0) ? "true" : "" %>"
		              aria-label="Slide <%= i + 1 %>"></button>
		    <% } %>
		  </div>
		
		  <div class="carousel-inner ratio ratio-21x9 rounded-4">
		    <% for (int i = 0; i < size; i++) {
		         ImageDto img = carouselImages.get(i);
		    %>
		      <div class="carousel-item <%= (i == 0) ? "active" : "" %>">
		        <img src="<%= request.getContextPath() %>/show.img?imageName=<%= img.getImageSavedName() %>"
						     class="d-block w-100"
						     alt="<%= img.getImageOriginalName() %>">
		      </div>
		    <% } %>
		  </div>
		
		  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
		    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
		    <span class="visually-hidden">Previous</span>
		  </button>
		  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
		    <span class="carousel-control-next-icon" aria-hidden="true"></span>
		    <span class="visually-hidden">Next</span>
		  </button>
		</div>
		<% } else { %>
		  <p>등록된 이미지가 없습니다.</p>
		<% } %>
	</div>