<%@page import="model.room.RoomDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="container my-5">
		<form action="${pageContext.request.contextPath}/booking/submit" method="post">
    	<!-- 이부분 필수 -->
    	<input type="hidden" name="bookStayNum" value="${param.stayNum}"/>
        <div class="row g-5">
            <!-- 왼쪽: 예약자 정보 -->
            <div class="col-md-7">
                <div class="left-container">
                    <h1>예약자 정보</h1>
                    <input type="hidden" name="usersNum" id="userNum" value="${usersNum}"/>

                    <div class="mb-3">
                        <label for="usersName" class="form-label">이름</label>
                        <p>${usersName}</p>
                        <input type="hidden" name="usersName" id="userName" value="${usersName}"/>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">이메일</label>
                        <p>${email}</p>
                        <input type="hidden" name="email" id="email" value="${email}"/>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">전화번호</label>
                        <p>${phone}</p>
                        <input type="hidden" name="phone" id="phone" value="${phone}"/>
                    </div>

                    <div class="mb-4">
                        <h2>옵션</h2>
                        <p>숙박 시 제공되는 옵션을 선택하세요</p>

                        <label class="form-label">추가 침대</label><br/>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="bed" value="extraBed" id="extraBed" data-label="간이 침대"/>
                            <label class="form-check-label" for="extraBed">간이 침대</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="bed" value="infantBed" id="infantBed" data-label="유아 침대"/>
                            <label class="form-check-label" for="infantBed">유아 침대</label>
                        </div>

                        <div class="mt-3">
                            <label class="form-label">도착</label><br/>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="checkInTime" value="standard" id="checkInStandard" checked data-label="정규 시간"/>
                                <label class="form-check-label" for="checkInStandard">정규 시간</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="checkInTime" value="early" id="checkInEarly" data-label="이른 체크인"/>
                                <label class="form-check-label" for="checkInEarly">이른 체크인</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="checkInTime" value="late" id="checkInLate" data-label="늦은 도착"/>
                                <label class="form-check-label" for="checkInLate">늦은 도착</label>
                            </div>
                        </div>
                    </div>

                    <div class="mb-4">
                        <h2>추가 요청사항</h2>
                        <textarea name="bookRequest" id="bookRequest" class="form-control" rows="5" placeholder="요청사항을 입력해 주세요."></textarea>
                    </div>

                    <h2>예약 안내사항</h2>
					<p>
					    정확한 객실 요금은 일정 선택 후 확인할 수 있습니다.<br/>
					    객실 타입에 따라 최대 투숙 인원이 상이합니다.<br/>
					    스탠다드 객실 : 기준 인원 2인, 최대 인원 2인까지 이용할 수 있습니다.<br/>
					    트윈 객실 : 기준 인원 2인, 최대 인원 3인까지 이용할 수 있습니다.<br/>
					    패밀리 객실 : 기준 인원 2인, 최대 인원 4인까지 이용할 수 있습니다.<br/>
					    기준 인원 초과 시, 1인 1박당 추가 요금이 발생합니다.<br/><br/>
					    
					    예약 가능일<br/>
					    당일 예약 및 입실이 가능합니다.<br/><br/>
					    
					    반려 동물<br/>
					    반려 동물은 동반이 불가한 숙소입니다.
					</p>

                    <h2>이용 안내</h2>
                    <p>
                    	체크인은 오후 3시 이후부터, 체크아웃은 오전 11시까지 입니다.<br/>
						예약 시, 최대 인원을 초과하는 인원은 입실이 불가합니다.<br/>
						예약 인원 외 방문객의 출입은 제한됩니다.<br/>
						숙소 내의 모든 공간에서 절대 금연입니다. 위반 시 특수청소비가 청구됩니다.<br/>
						침구나 비품에 심각한 오염이 발생한 경우, 파손 및 분실 등에 변상비가 청구됩니다.<br/>
						늦은 시간 외부로 소음이 발생하지 않도록 배려 부탁드립니다.<br/>
						협의되지 않은 상업 사진 및 영상 촬영(광고용, 제품사진, 쇼핑몰, SNS마켓 포함), 드론 촬영은 불가합니다.<br/>
                    </p>
                    <h2>환불 규정</h2>
                    <p class=>
                    	체크인 10일 전까지 : 총 결제금액의 100% 환불<br/>
						체크인 9일 전까지 : 총 결제금액의 90% 환불<br/>
						체크인 8일 전까지 : 총 결제금액의 80% 환불<br/>
						체크인 7일 전까지 : 총 결제금액의 70% 환불<br/>
						체크인 6일 전까지 : 총 결제금액의 60% 환불<br/>
						체크인 5일 전까지 : 총 결제금액의 50% 환불<br/>
						체크인 4일 전까지 : 총 결제금액의 40% 환불<br/>
						체크인 3일 전부터 : 변경 및 환불 불가
                    </p>
               
                    <!-- <h2>판매자 정보</h2> -->
                </div>
            </div>
            <!-- 오른쪽: 예약 요약 -->
            <div class="col-md-5">
                <div class="right-container-fixed">
                    <input type="hidden" name="bookStayNum" id="bookStayNum" value="${stay.stayNum}" />

                    <select name="bookRoomNum" id="bookRoomNum" class="form-select">
                        <option value="" disabled selected>객실을 선택하세요</option>
                        <% 
                            List<RoomDto> roomList = (List<RoomDto>) request.getAttribute("roomList");
                            if (roomList != null && !roomList.isEmpty()) {
                                for (RoomDto room : roomList) {
                        %>
                            <option value="<%= room.getRoomNum() %>" data-price="<%= room.getRoomPrice() %>" data-name="<%= room.getRoomName() %>">
                                <%= room.getRoomName() %>
                            </option>
                        <% 
                                }
                            } else { 
                        %>
                            <option disabled>객실 정보가 없습니다</option>
                        <% } %>
                    </select>

                    <div class="mb-3 mt-3">
                        <label for="checkIn" class="form-label">체크인 날짜</label>
                        <input type="date" name="checkIn" id="checkIn" class="form-control"/>

                        <label for="checkOut" class="form-label mt-2">체크아웃 날짜</label>
                        <input type="date" name="checkOut" id="checkOut" class="form-control"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">투숙 인원 선택</label>
                        
                        <!-- 성인 -->
                        <div class="count-box">
                            <span class="me-auto">성인<br/><small class="text-muted">만 12세 이상</small></span>
                            <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('adult', -1)">-</button>
                            <span id="adultCount">2</span>
                            <input type="hidden" name="adult" id="adult" value="2" min="0"/>
                            <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('adult', 1)">+</button>
                        </div>

                        <!-- 어린이 -->
                        <div class="count-box">
                            <span class="me-auto">어린이<br/><small class="text-muted">만 2~11세</small></span>
                            <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('children', -1)">-</button>
                            <span id="childrenCount">0</span>
                            <input type="hidden" name="children" id="children" value="0"/>
                            <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('children', 1)">+</button>
                        </div>

                        <!-- 유아 -->
                        <div class="count-box">
                            <span class="me-auto">유아<br/><small class="text-muted">만 2세 미만</small></span>
                            <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('infant', -1)">-</button>
                            <span id="infantCount">0</span>
                            <input type="hidden" name="infant" id="infant" value="0"/>
                            <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('infant', 1)">+</button>
                        </div>

                        <strong class="mt-2">총 인원 : <span id="totPerson">1</span>명</strong>
                    </div>

                    <div class="mb-4">
                        <strong>객실 이름 :</strong> <span id="selectedRoomNameDisplay"></span><br/>
                        <strong>객실 가격 :</strong> ₩ <span id="roomPrice">선택하세요</span><br />
                        <strong>추가 침대 :</strong> <span id="bedOption"></span><br/>
                        <strong>도착 시간 :</strong> <span id="checkInOption"></span><br/>
                        <strong>총액 :</strong> ₩ <span id="totalAmount"></span>
                    </div>

                    <!-- JS에서 설정한 값을 서블릿에 넘기기 위한 hidden input들 -->
                    <input type="hidden" name="selectedBed" id="selectedBed" value=""/>
                    <input type="hidden" name="selectedCheckInTime" id="selectedCheckInTime" value="standard"/>
                    <input type="hidden" name="totalAmountValue" id="totalAmountValue" value="0"/>

                    <button type="submit" class="btn btn-primary w-100">예약하기</button>
                </div>
            </div>
        </div>
     </form>
</div>

<script>
	window.addEventListener("DOMContentLoaded", () => {
	    const pax = {
	        adult: parseInt(document.querySelector("#adult").value) || 0,
	        children: parseInt(document.querySelector("#children").value) || 0,
	        infant: parseInt(document.querySelector("#infant").value) || 0
	    };
	
	    const updateUI = () => {
	        document.querySelector("#adultCount").textContent = pax.adult;
	        document.querySelector("#childrenCount").textContent = pax.children;
	        document.querySelector("#infantCount").textContent = pax.infant;
	        document.querySelector("#totPerson").textContent = pax.adult + pax.children + pax.infant;
	
	        document.querySelector("#adult").value = pax.adult;
	        document.querySelector("#children").value = pax.children;
	        document.querySelector("#infant").value = pax.infant;
	    };
	
	    window.changeCount = (type, delta) => {
	        if (pax.hasOwnProperty(type)) {
	            pax[type] = Math.max(0, pax[type] + delta);
	            updateUI();
	            calculateTotalAmount();
	            updateSummaryBox();
	        }
	    };
	
	    let isBedTouched = false;
	    let isCheckInTimeTouched = false;
	
	    // 침대 옵션 처리
	    document.querySelectorAll('input[name="bed"]').forEach(cb => {
	        cb.addEventListener('change', () => {
	            isBedTouched = true;
	            const selectedBeds = Array.from(document.querySelectorAll('input[name="bed"]:checked'))
	                .map(b => b.value);
	            document.querySelector('#selectedBed').value = selectedBeds.join(',');
	            updateSummaryBox();
	        });
	    });
	
	    // 체크인 시간 처리
	    document.querySelectorAll('input[name="checkInTime"]').forEach(radio => {
		    radio.addEventListener('change', (e) => {
		        isCheckInTimeTouched = true;
		        document.querySelector('#selectedCheckInTime').value = e.target.value;
		
		        console.log("선택된 라디오:", e.target);
		        console.log("해당 id:", e.target.id);
		        console.log("label 요소:", document.querySelector(`label[for="${e.target.id}"]`));
		
		        updateSummaryBox();
		    });
		});
	
	    function calculateTotalAmount() {
	        const pricePerDay = getSelectedRoomPrice();
	        const days = calculateStayDays();
	        const total = pricePerDay * days;
	        document.querySelector('#totalAmountValue').value = total;
	    }
	
	    function getSelectedRoomPrice() {
	        const select = document.querySelector('#bookRoomNum');
	        if (!select || !select.value) return 0;
	        const selectedOption = select.options[select.selectedIndex];
	        const price = selectedOption.getAttribute('data-price');
	        return price ? parseInt(price, 10) : 0;
	    }
	
	    function calculateStayDays() {
	        const checkIn = document.querySelector('#checkIn').value;
	        const checkOut = document.querySelector('#checkOut').value;
	        if (!checkIn || !checkOut) return 0;
	        const inDate = new Date(checkIn);
	        const outDate = new Date(checkOut);
	        const diff = (outDate - inDate) / (1000 * 60 * 60 * 24);
	        return diff > 0 ? diff : 0;
	    }
	
	    function updateSummaryBox() {
	        // 침대 옵션
	        const beds = Array.from(document.querySelectorAll('input[name="bed"]:checked')).map(cb => {
	            if (cb.value === "extraBed") return "간이 침대";
	            if (cb.value === "infantBed") return "유아 침대";
	            return "";
	        }).filter(Boolean);
	
	        const bedOption = document.querySelector("#bedOption");
	        if (bedOption) {
	            bedOption.textContent = beds.length > 0 ? beds.join(", ") : "선택된 옵션이 없습니다";
	        }
	
	        // 체크인 시간 옵션: 기본값은 '정규 시간'
	        const checkInRadio = document.querySelector('input[name="checkInTime"]:checked');
	        const checkInOption = document.querySelector("#checkInOption");

	        let checkInLabel = "정규 시간";  // 기본값
	        if (checkInRadio) {
	            const labelEl = checkInRadio.closest('.form-check')?.querySelector('label');
	            if (labelEl) checkInLabel = labelEl.textContent;
	        }

	        if (checkInOption) {
	            checkInOption.textContent = checkInLabel;
	        }
	
	        // 총 인원
	        const totPerson = pax.adult + pax.children + pax.infant;
	        const totPersonEl = document.querySelector("#totPerson");
	        if (totPersonEl) {
	            totPersonEl.textContent = totPerson;
	        }
	        
	        // 총액 표시
	        const totalAmountEl = document.querySelector("#totalAmount");
	        const totalAmountValue = parseInt(document.querySelector("#totalAmountValue").value, 10) || 0;
	        if (totalAmountEl) {
	            totalAmountEl.textContent = totalAmountValue.toLocaleString() + "원";
	        }

	    }
	
	    function updateRoomInfo() {
	        const roomSelect = document.querySelector("#bookRoomNum");
	        if (!roomSelect.value) {
	            document.querySelector("#selectedRoomNameDisplay").textContent = "선택된 객실이 없습니다";
	            document.querySelector("#roomPrice").textContent = "0원";
	            return;
	        }
	
	        const selectedOption = roomSelect.options[roomSelect.selectedIndex];
	        const name = selectedOption.getAttribute("data-name");
	        const price = selectedOption.getAttribute("data-price");
	        console.log("선택 객실:", name, price);
	
	        document.querySelector("#selectedRoomNameDisplay").textContent = name;
	        document.querySelector("#roomPrice").textContent = Number(price).toLocaleString() + "원";
	    }
	
	    // 이벤트 바인딩
	    document.querySelector("#bookRoomNum").addEventListener("change", () => {
	        updateRoomInfo();
	        calculateTotalAmount();
	        updateSummaryBox();
	    });
	    document.querySelector("#checkIn").addEventListener("change", () => {
	        calculateTotalAmount();
	        updateSummaryBox();
	    });
	    document.querySelector("#checkOut").addEventListener("change", () => {
	        calculateTotalAmount();
	        updateSummaryBox();
	    });
	
	    // 초기 실행
	    updateUI();
	    updateRoomInfo();
	    calculateTotalAmount();
	    updateSummaryBox();
	});
</script>
