<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String roomName="test";
	String roomType="test";
	String roomPaxMax="test";
	String roomPrice="test";
	String roomNum="test";
%>
<div class="container my-5">
    <div class="row">
        <!-- 객실 타입 카드 영역 -->
        <div class="col-md-8">
            <div class="card mb-4" onclick="toggleDetail(<%= roomNum%>)" style="cursor: pointer;">
                <div class="row g-0">
                
                    <!-- 왼쪽 숙소 이미지 영역 -->
                    <div class="col-md-5">
                    	<!--  ! 테스트 이미지   -->
                    	<img src="/images/indexc01.jpg" class="img-fluid h-100 room-image" alt="임시로 객실 이미지">
                    </div>

                    <!-- 오른쪽: 객실 정보 텍스트 및 객실 선택 버튼 영역 -->
                    <div class="col-md-7 p-4 d-flex flex-column justify-content-between">
                        <div>
                            <h5 class="card-title"><%=roomName%></h5>
                            <p class="card-text">
                                타입: <%= roomType%> /
                                최대 인원: <%= roomPaxMax %>명 /
                                가격: ₩<%=roomPrice%>
                            </p>
                        </div>

                        <div class="mt-3 d-flex justify-content-between align-items-center">
                            <button type="button" class="btn btn-room-select"
                                onclick="event.stopPropagation(); selectRoom('<%=roomNum %>', '<%= roomName%>', <%=roomPrice %>)">
                                객실 선택
                            </button>
                        </div>

                    </div>
                </div>
            </div>
        </div>

    </div>
</div>