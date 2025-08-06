<%@page import="model.inq.InquiryDao"%>
<%@page import="model.inq.InquiryDto"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.tomcat.jakartaee.commons.lang3.StringUtils"%>
<%@page import="model.page.StayDao"%>
<%@page import="model.page.StayDto"%>
<%@page import="java.util.List"%>
<%@page import="model.user.UserDao"%>
<%@page import="model.user.UserDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	long num = Long.parseLong(request.getParameter("num"));
	InquiryDto inqDto=InquiryDao.getInstance().getByNum(num);
	long usersNum=inqDto.getUsersNum();
	UserDto userDto=UserDao.getInstance().getBasicInfoByNum(usersNum);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/inquiry/answer-form.jsp</title>
<jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
<style>
	.form-section {
    	border-top: 1px solid #dee2e6;
        border-bottom: 1px solid #dee2e6;
        padding: 0.5rem;
        margin: 1rem;
	}

    .section-title {
    	font-weight: bold;
        margin-bottom: 0.5rem;
	}
    .agree-txt{
		background: #fff;
		height: 150px;
		overflow-y: auto;
		padding: 20px;
		border: 1px solid #d9d9d9;
		font-size: 12px;
		color: #666;
		margin-top: 15px;
		border-radius: 8px;
		box-sizing: border-box;						
	}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>
	<div class="text-center mt-5 mb-5">
		<h1 class="pb-4">문의 상세보기</h1>
	</div>
	<div class="modal fade" id="alertModal" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
	    	<div class="modal-content">
	    		<div class="modal-header">
	       			<h5 class="modal-title">알림</h5>
	        		<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	      		</div>
	      		<div class="modal-body" id="alertModalBody">
	        		<!-- 메시지 삽입 -->
	      		</div>
	      		<div class="modal-footer">
	        		<button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
	      		</div>
	    	</div>
	  	</div>
	</div>	
	<div class="container">
		<form action="${pageContext.request.contextPath }/updateInquiry.inq" method="post" id="answerForm">
        	<div class="form-section">
            	<span class="form-label">문의할 숙소</span>
                <input type="text" name="userPhone" class="bg-light border-1 rounded" value="<%=inqDto.getStayName()!=null ? inqDto.getStayName() : "기타" %>" readonly />
			</div>
            <div class="form-section">
            	<span class="form-label">문의유형</span>
            	<label>
            		<input type="radio" name="inqType" value="예약" disabled/> 예약
            	</label>
            	<label>
            		<input type="radio" name="inqType" value="결제" disabled/> 결제
            	</label>
            	<label>
            		<input type="radio" name="inqType" value="숙소정보" disabled/> 숙소정보
            	</label>
            	<label>
	            	<input type="radio" name="inqType" value="시설문의" disabled/> 시설문의
            	</label>
            	<label>
            		<input type="radio" name="inqType" value="기타" checked disabled/> 기타
            	</label>
            </div>
			<div class="form-section">
            	<div class="d-flex">
	        		<div>
	                	<p>이름</p>
	                    <p>전화번호</p>
	                    <p>이메일</p>
	                </div>
	                <div class="ms-5">
	                    <p><%=userDto.getUsersName() %></p>
	                    <p><%=userDto.getUsersPhone() %></p>
	                    <p><%=userDto.getUsersEmail() %></p>
	                </div>
            	</div>
            	<small>회원정보를 확인해주세요.</small>
            </div>
            <div class="form-section">
            	<p class="form-label mt-2">문의내용</p>
            	<div class="form-control bg-light border-1 mt-2" style="height: auto; min-height: 150px;">
				    <%=inqDto.getTitle() %>
				</div>
				<div class="form-control bg-light border-1 mt-2" style="height: auto; min-height: 150px;">
				    <%=inqDto.getContent().replaceAll("\n", "<br>") %>
				</div>
            </div>
            <div class="form-section">
             	<div>
            		<label for="answer" class="form-label">답변</label>
            		<textarea class="form-control" name="answer" id="answer"></textarea>
            	</div>
            </div>
            <input type="hidden" name="num" value="<%=num %>" />
            <div class="d-flex justify-content-end">
	            <button type="submit" class="btn btn-primary">저장</button>
	            <button type="reset" class="btn btn-outline-primary ms-2 me-4">초기화</button>
			</div>
		</form>
	</div>
    <jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
    <script>
	    const showAlertModal = (message) => {
	        const modalBody = document.getElementById("alertModalBody");
	        modalBody.textContent = message;
	        const modal = new bootstrap.Modal(document.getElementById("alertModal"));
	        modal.show();
	    };
    	const form = document.querySelector("#answerForm");
    	form.addEventListener("submit", (e)=>{
            e.preventDefault();
            const answer = document.querySelector("#answer");
            if(answer.value.trim()==""){
            	showAlertModal("답변을 작성해주세요.");
                answer.focus();
                return;
            }

            showAlertModal("답변이 저장되었습니다.");
            form.submit();
        });
    </script>
</body>
</html>