<%@page import="model.page.ScopeDao"%>
<%@page import="model.page.ScopeDto"%>
<%@page import="model.room.RoomDao"%>
<%@page import="java.util.List"%>
<%@page import="model.room.RoomDto"%>
<%@page import="model.page.PageDto"%>
<%@page import="model.page.PageDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="model.page.StayDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//get 방식 파라미터로 전달되는 글 번호 얻어내기
	String strPageNum = request.getParameter("pageNum");
	
	long pageNum = Long.parseLong(strPageNum);
	
	// 페이지 정보 가져오기
	PageDto pageDto = PageDao.getInstance().getBy(pageNum);
	
	// 스테이 정보 가져오기
	long stayNum = pageDto.getStayNum();
	StayDao stayDao = StayDao.getInstance();
	
	// 객실 정보
	List<RoomDto> list = RoomDao.getInstance().getListByStayNum((Long)stayNum);
	
	// 유저 정보
    Long usersNum = (Long)session.getAttribute("usersNum");
    String usersId = (String)session.getAttribute("usersId");
    
	// 별점 리뷰
	double avgStar = stayDao.getAverageStar(stayNum);
	int reviewCount = stayDao.getReviewCount(stayNum);
	
	// 리뷰 리스트
	List<ScopeDto> reviewList = ScopeDao.getInstance().getReviewList(stayNum);
	
	// 좌표
	//double stayLat = Double.parseDouble(stayDto.getStayLat());
	//double stayLong = Double.parseDouble(stayDto.getStayLong());
	
	//로그인 했는지 여부를 알아내기
	boolean isLogin  = usersId == null ? false : true;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/page/view</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>

	<!-- Carousel 영역 -->


    <div class="container py-4">
		<!-- 숙소 정보 -->
		<div class="d-flex justify-content-between align-items-start">
			<div>
				<h2 class="fw-bold"><%=pageDto.getStayName() %></h2>
				<p class="text-muted mb-1"><%=pageDto.getStayLoc() %></p>
				<a href="#review" class="mb-1">★ <%=String.format("%.1f", avgStar) %>점 / <%=reviewCount %>개</a>
			</div>

			<div class="text-end">
				<div class="mb-2">
					<button class="btn btn-outline-secondary btn-sm me-1">
						<i class="bi bi-share"></i>
					</button>
					<button class="btn btn-outline-secondary btn-sm">
						<i class="bi bi-bookmark"></i>
					</button>
				</div>
				<button type="button" class="btn btn-dark btn-booking" onclick="location.href='${pageContext.request.contextPath}/booking/booking-page.jsp?stayNum=<%=pageDto.getStayNum() %>'">예약하기</button>
			</div>
		</div>

		<!-- FACILITIES / 미반영 -->
		<div class="mt-4 border-top pt-3">
			<h6 class="fw-bold">편의시설</h6>
			<div class="d-flex flex-wrap gap-4 mt-2 text-muted">
				<div><i class="bi bi-bucket me-1"></i>욕조</div>
				<div><i class="bi bi-tv me-1"></i>닌텐도</div>
			</div>
		</div>

		<!-- 네비게이션 바 -->
		<ul class="nav nav-tabs mt-5" id="stayTabs">
			<li class="nav-item">
				<a class="nav-link active" href="#info">스테이 소개</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#review">리뷰</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#loc">위치 및 정보</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#notice">안내사항</a>
			</li>
		</ul>

		<!-- 스테이 소개 -->
		<div id="info" class="py-4">
			<h4 class="fw-bold">집을 닮은 한 그루의 공간</h4>
			<p class="text-muted"><%=pageDto.getPageContent() %></p>
		</div>

		<!-- 리뷰 작성 -->
		<div id="review" class="py-4">
			<h4 class="fw-bold">리뷰</h4>
			<p class="text-muted">총 <%=reviewCount %> 개 / 평균 <%=String.format("%.1f", avgStar) %>점</p>
			<form action="scope-save.jsp" method="post" enctype="multipart/form-data" class="border rounded p-4 my-4 shadow-sm">
				<input type="hidden" name="stayNum" value="<%=stayNum%>">
				<input type="hidden" name="usersId" value="<%=usersId%>">

				<!-- 별점 -->
				<div class="mb-3">
					<label class="form-label">별점 선택</label>
					<div id="starContainer">
						<% for (int i = 1; i <= 5; i++) { %>
							<i class="bi bi-star star" data-value="<%=i%>" style="font-size: 2rem; cursor: pointer;"></i>
						<% } %>
					</div>
					<input type="hidden" id="scopeStar" name="scopeStar" value="0">
				</div>

				<!-- 텍스트 -->
				<div class="mb-3">
					<label class="form-label">리뷰 내용</label>
					<textarea name="scopeContent" rows="4" class="form-control" placeholder="리뷰 입력 " required></textarea>
				</div>

				<!-- 이미지 -->
				<div class="mb-3">
					<label class="form-label">이미지 첨부 (선택)</label>
					<input type="file" name="image" accept="image/*" class="form-control">
				</div>

				<button type="submit" class="btn btn-primary">리뷰 등록</button>
			</form>
		</div>

		<!-- 리뷰 목록 -->
		<div class="reviews">
			<%for(ScopeDto scope : reviewList){ %>
				<div class="card mb-3">
					<div class="card-body position-relative">
						<div class="d-flex justify-content-between">
							<strong><%=scope.getUsersId() %></strong>
							<small><%=scope.getScopeCreatedAt() %></small>
						</div>

						<div class="mb-2">
							<%for(int i = 1; i <= 5; i++){ %>
								<%if(i <= scope.getScopeStar()){ %>
									<i class="bi bi-star-fill text-warning"></i>
								<%}else{ %>
									<i class="bi bi-star text-warning"></i>
								<%} %>
							<%} %>
						</div>

						<pre><%= scope.getScopeContent() %></pre>

						<%if(scope.getUserNum() == usersNum) { %>
							<!-- 수정 버튼 -->
							<button class="btn btn-sm btn-outline-secondary edit-btn">수정</button>

							<!-- 삭제 버튼 -->
							<button class="btn btn-sm btn-outline-danger delete-btn" data-scope-num="<%=scope.getScopeNum() %>">삭제</button>

							<!-- 수정 폼 (처음에 숨김) -->
							<div class="edit-form d-none mt-2">
								<form action="update-scope.jsp" method="post">
									<input type="hidden" name="scopeNum" value="<%=scope.getScopeNum() %>">
									<textarea name="scopeContent" class="form-control mb-2"><%=scope.getScopeContent() %></textarea>
									<button type="submit" class="btn btn-sm btn-success">수정 완료</button>
									<button type="button" class="btn btn-sm btn-secondary cancel-edit-btn">취소</button>
								</form>
							</div>
						<%} %>
					</div>
				</div>
			<%} %>
		</div>

		<!-- 위치 및 정보 -->
		<div id="loc" class="py-4">
			<h4 class="fw-bold">위치 및 정보</h4>
		</div>

		<!-- 안내사항 -->
		<div id="notice" class="py-4">
			<h4 class="fw-bold">안내사항</h4>
			<div class="accordion" id="accordionExample">
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#navOne" aria-expanded="true" aria-controls="collapseOne">
							예약 안내
						</button>
					</h2>
					<div id="navOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
						<div class="accordion-body">
							<table>
								<thead>
									<tr>
										<th>객실</th>
										<th>인원(기준/최대)</th>
										<th>금액</th>
									</tr>
								</thead>
								<tbody>
									<%for (RoomDto room : list) { %>
										<tr>
											<td><%=room.getRoomName() %></td>
											<td>(<%=room.getRoomAdultMax() %> / <%=room.getRoomPaxMax() %>)명</td>
											<td><%=String.format("%,d", room.getRoomPrice()) %></td>
										</tr>
                					<%} %>
								</tbody>
							</table>
							<%=pageDto.getPageReserve() %>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#navTwo" aria-expanded="false" aria-controls="collapseTwo">
							이용 안내
						</button>
					</h2>
					<div id="navTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
						<div class="accordion-body">
							<%=pageDto.getPageGuide() %>
						</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#navThree" aria-expanded="false" aria-controls="collapseThree">
							환불 규정
						</button>
					</h2>
					<div id="navThree" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
						<div class="accordion-body">
							<%=pageDto.getPageRefund() %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		// 클라이언트 로그인 여부
    	const isLogin = <%=isLogin %>;

		document.querySelector(".scopeContent").addEventListener("input", ()=>{
    		// 원글의 댓글 입력란에 입력했을 때 만일 로그인 하지 않았다면
    		if(!isLogin){
    			alert("댓글 작성을 위해 로그인이 필요합니다!");
    			location.href=
    				"${pageContext.request.contextPath }/user/loginform.jsp?url=${pageContext.request.contextPath }/board/view.jsp?pageNum=<%=pageNum %>";
    		}
    	});

		// 수정 버튼 클릭 시 입력란 보이기
		document.querySelectorAll(".edit-btn").forEach(btn => {
			btn.addEventListener("click", () => {
				const editForm = btn.nextElementSibling.nextElementSibling;
				editForm.classList.remove("d-none");
				btn.classList.add("d-none");
			});
		});

		// 수정 취소 버튼 클릭 시 숨기기
		document.querySelectorAll(".cancel-edit-btn").forEach(btn => {
			btn.addEventListener("click", () => {
				const form = btn.closest(".edit-form");
				form.classList.add("d-none");
				form.previousElementSibling.previousElementSibling.classList.remove("d-none"); // 수정 버튼 다시 보이기
			});
		});

		// 삭제 버튼 클릭 시 확인 => 삭제
		document.querySelectorAll(".delete-btn").forEach(btn => {
			btn.addEventListener("click", () => {
				const scopeNum = btn.getAttribute("data-scope-num");
				if (confirm(scopeNum + "번 리뷰를 삭제하시겠습니까?")) {
					location.href = `<%=request.getContextPath()%>/scope/scope-delete.jsp?scopeNum=${scopeNum}&stayNum=<%=stayNum%>`;
				}
			});
		});

		// 별점
		document.querySelectorAll('.star').forEach(star => {star.addEventListener('click', function() {
				const value = this.getAttribute('data-value');
				document.getElementById('scopeStar').value = value;
				document.querySelectorAll('.star').forEach(s => {
					s.classList.remove('text-warning');
					s.classList.add('text-secondary');
				});
				for (let i = 0; i < value; i++) {
					document.querySelectorAll('.star')[i].classList.remove('text-secondary');
					document.querySelectorAll('.star')[i].classList.add('text-warning');
				}
			});
		});
	</script>
</body>
</html>