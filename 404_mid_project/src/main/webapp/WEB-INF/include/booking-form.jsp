<%@page import="model.room.RoomDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <div class="container my-5">
        <form action="${pageContext.request.contextPath}/booking/submit" method="post" id="bookingForm">
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
                        <h2>이용 안내</h2>
                        <h2>환불 규정</h2>
                        <h2>판매자 정보</h2>
                    </div>
                </div>

                <!-- 오른쪽: 예약 요약 -->
                <div class="col-md-5">
                    <div class="right-container-fixed">
                        <!-- <input type="hidden" name="bookStayNum" id="bookStayNum" value="100"/> -->
                         <input type="hidden" name="bookStayNum" id="bookStayNum" value="${stay.stayNum}" />
					
						<!-- <label for="bookRoomNum">객실 선택</label>
						<select id="bookRoomNum" name="bookRoomNum">
							<option value="" disabled selected hidden>객실을 선택하세요</option>
							<option value="1">스탠다드 객실</option>
							<option value="2">트윈 객실</option>
							<option value="3">패밀리 객실</option>
						</select> -->
						
						<select name="bookRoomNum" id="bookRoomNum" class="form-select">
						    <option value="" disabled selected>객실을 선택하세요</option>
						    <% 
						       List<RoomDto> roomList = (List<RoomDto>) request.getAttribute("roomList");
						       if(roomList != null && !roomList.isEmpty()) {
						           for(RoomDto room : roomList) {
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
                            <div class="count-box">
                                <span class="me-auto">성인<br/><small class="text-muted">만 12세 이상</small></span>
                                <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('adult', -1)">−</button>
                                <span id="adultCount">2</span>
                                <input type="hidden" name="adult" id="adult" value="2" min="0"/>
                                <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('adult', 1)">＋</button>
                            </div>

                            <div class="count-box">
                                <span class="me-auto">어린이<br/><small class="text-muted">만 2~11세</small></span>
                                <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('children', -1)">−</button>
                                <span id="childrenCount">0</span>
                                <input type="hidden" name="children" id="children" value="0"/>
                                <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('children', 1)">＋</button>
                            </div>

                            <div class="count-box">
                                <span class="me-auto">유아<br/><small class="text-muted">만 2세 미만</small></span>
                                <button type="button" class="btn btn-outline-secondary count-btn" onclick="changeCount('infant', -1)">−</button>
                                <span id="infantCount">0</span>
                                <input type="hidden" name="infant" id="infant" value="0"/>
                                <button type="button" class="btn btn-outline-primary count-btn" onclick="changeCount('infant', 1)">＋</button>
                            </div>
                            <strong class="mt-2">총 인원 : <span id="totPerson">1</span>명</strong>
                        </div>

                        <div class="mb-4">
                            <!-- 객실명도 표시 -->
							<strong>객실 이름:</strong> <span id="selectedRoomNameDisplay"></span><br/>
							<!-- <strong>객실 요금:</strong> <span id="roomPrice"></span><br/>-->
							<strong>객실 가격:</strong> <i class="bi bi-currency-dollar"></i><span id="roomPrice">선택하세요</span><br />
							<strong>추가 침대:</strong> <span id="bedOption"></span><br/>
							<strong>도착 시간:</strong> <span id="checkInOption"></span><br/>
							<strong>총액:</strong>
							<input type="text" name="totalAmount" id="totalAmount" class="form-control mt-1" readonly/>
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
		document.addEventListener("DOMContentLoaded", function () {
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

    // 표시 여부 제어 플래그
    let isBedTouched = false;
    let isCheckInTimeTouched = false;

    // ✅ 침대 체크박스
    document.querySelectorAll('input[name="bed"]').forEach(cb => {
        cb.addEventListener('change', () => {
            isBedTouched = true;
            const selected = Array.from(document.querySelectorAll('input[name="bed"]:checked'))
                .map(cb => cb.value);
            document.querySelector("#selectedBed").value = selected.join(",");
            updateSummaryBox();
        });
    });

    // ✅ 체크인 라디오
    document.querySelectorAll('input[name="checkInTime"]').forEach(radio => {
        radio.addEventListener('change', () => {
            isCheckInTimeTouched = true;
            document.querySelector('#selectedCheckInTime').value = radio.value;
            updateSummaryBox();
        });
    });

    // ✅ 총액 계산
    function calculateTotalAmount() {
        const pricePerDay = getSelectedRoomPrice();
        const days = calculateStayDays();
        const total = pricePerDay * days;
        document.querySelector('#totalAmountValue').value = total;
        document.querySelector('#totalAmount').value = total > 0 ? total.toLocaleString() + '원' : "";
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

    // ✅ 요약 박스 업데이트 (data-label 방식)
    function updateSummaryBox() {
        // ➤ 침대 옵션
        const beds = Array.from(document.querySelectorAll('input[name="bed"]:checked'))
            .map(cb => cb.dataset.label || "")
            .filter(Boolean);
        document.querySelector("#bedOption").textContent = isBedTouched
            ? (beds.length > 0 ? beds.join(", ") : "없음")
            : "";

        // ➤ 체크인 시간
        const checkInRadio = document.querySelector('input[name="checkInTime"]:checked');
        const checkInLabel = checkInRadio?.dataset.label || "";
        document.querySelector("#checkInOption").textContent = isCheckInTimeTouched ? checkInLabel : "";

        // ➤ 총 인원
        const totPerson = pax.adult + pax.children + pax.infant;
        document.querySelector("#totPerson").textContent = totPerson;
    }

    // ✅ 객실명/가격 표시
    function updateRoomInfo() {
        const select = document.querySelector("#bookRoomNum");
        const nameEl = document.querySelector("#selectedRoomNameDisplay");
        const priceEl = document.querySelector("#roomPrice");

        if (!select.value) {
            nameEl.textContent = "선택하세요";
            priceEl.textContent = "0 원";
            return;
        }

        const selectedOption = select.options[select.selectedIndex];
        const name = selectedOption.getAttribute("data-name");
        const price = selectedOption.getAttribute("data-price");

        nameEl.textContent = name;
        priceEl.textContent = Number(price).toLocaleString() + " 원";
    }

    // ✅ 이벤트 바인딩
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

    // ✅ 초기 실행
    updateUI();
    updateRoomInfo();
    calculateTotalAmount();
    updateSummaryBox();
		});
</script>
