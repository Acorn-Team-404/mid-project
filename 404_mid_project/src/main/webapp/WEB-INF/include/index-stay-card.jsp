<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.admin.StaySummaryDto"%>
<%@ page import="model.admin.StayInfoDao"%>

<%
    // 한 번에 조회된 DTO 리스트 
    List<StaySummaryDto> list = StayInfoDao.getInstance().getStaySummaries();
		
%>
<div class="container gx-1 w-75 mx-auto my-4">
	<jsp:include page="/WEB-INF/include/index-carousel.jsp"></jsp:include>
  <!-- ── 섹션 헤더 (화살표 없음) ── -->
  <div class="mb-3">
    <h3 class="mb-1">STAYLOG의 모든 숙소</h3>
    <small class="text-muted">합리적인 가격으로 검증된 공간 퀄리티를 경험하세요.</small>
  </div>
<div class="row">

<%  // ★ for 문 시작: dto 를 여기서 선언
    for (StaySummaryDto dto : list) {
        double avg    = dto.getAvgRating();               // 평균별점
        int    stars  = (int)Math.round(avg);             // 반올림해서 별개수
        String avgStr = String.format("%.1f", avg);       // 화면용 소수1자리
        String viewUrl = request.getContextPath()
                + "/page/page-view.jsp?stayNum=" 
                + dto.getStayNum();
        // 이미지 파일명 가져오기
        String imageName = dto.getImageName();
        String imageUrl;
        

        // 대체 이미지 경로 지정 (no-image.png가 webapp/images 폴더에 있다고 가정)
        if (imageName == null 
            || imageName.isBlank() 
            || "default.jpg".equals(imageName)) {
            imageUrl = request.getContextPath() + "/images/no-image.png";
        } else {
            imageUrl = request.getContextPath() + "/show.img?imageName=" + imageName;
        }
%>
  <div class="col-12 col-sm-6 col-md-4 col-lg-4">
    <div class="card listing-card position-relative shadow-sm mx-auto mb-4" style="max-width: 30rem; width: 100%;">
      <!-- 1) 이미지 -->
			<div class="ratio ratio-16x9">
			  <img
			    src="<%= imageUrl %>"
			    class="card-img-top"
			    alt="<%= dto.getStayName() %>"
			    style="object-fit: cover;"
			  />
			</div>

      <!-- 카드 본문 -->
      <div class="card-body mt-2">
        <h5 class="card-title mb-1"><%=dto.getStayName()%></h5>
        <p class="text-muted small mb-2"><%=dto.getStayLoc()%></p>

				<!-- ★★★★☆ (3.0)[3] -->
        <div class="star-rating mb-2">
        <%  for (int k = 1; k <= stars; k++) { %>
            <i class="bi bi-star-fill text-warning"></i>
        <%  }
            for (int k = stars + 1; k <= 5; k++) { %>
            <i class="bi bi-star text-warning"></i>
        <%  } %>
          <!-- 평균(소수1자리)과 리뷰 개수 -->
          <small class="text-muted">
            (<%=avgStr%>)[<%=dto.getReviewCount()%>]
          </small>
        </div>

        <!-- 최저가 -->
        <p class="fw-bold mb-1">₩<%=String.format("%,d", dto.getMinPrice())%>~</p>
				
	      <!-- ★ 여기에 예약 버튼 대신 'stretched-link' ★ -->
	      <a href="<%=viewUrl%>" class="stretched-link"></a>
	   	 </div> <!-- /.card-body -->

    </div>
  </div>
<%  }  // for 문 종료 %>
</div>
</div>
