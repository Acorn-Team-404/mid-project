const pax = {
    adult : parseInt(document.querySelector("#adult").value) || 0,
    children : parseInt(document.querySelector("#children").value) || 0,
    infant : parseInt(document.querySelector("#infant").value) || 0
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

// 초기 상태에 표시 여부 제어 변수
let isBedTouched = false;
let isCheckInTimeTouched = false;

// 2. 침대 옵션 체크박스 -> hidden input 세팅 + 표시 갱신
document.querySelectorAll('input[name="bed"]').forEach(cb => {
    cb.addEventListener('change', () => {
        isBedTouched = true;
        const selectedBeds = Array.from(document.querySelectorAll('input[name="bed"]:checked'))
            .map(b => b.value);
        document.querySelector('#selectedBed').value = selectedBeds.join(',');
        updateSummaryBox();
    });
});

// 3. 체크인 시간 라디오 -> hidden input 세팅 + 표시 갱신
document.querySelectorAll('input[name="checkInTime"]').forEach(radio => {
    radio.addEventListener('change', () => {
        isCheckInTimeTouched = true;
        document.querySelector('#selectedCheckInTime').value = radio.value;
        updateSummaryBox();
    });
});

// 4. 총액 계산용 함수
function calculateTotalAmount() {
    const pricePerDay = getSelectedRoomPrice();
    const days = calculateStayDays();
    const total = pricePerDay * days;
    document.querySelector('#totalAmountValue').value = total;
    document.querySelector('#totalAmount').value = total > 0 ? total.toLocaleString() + '원' : "";
}

// 객실 가격 가져오기
function getSelectedRoomPrice() {
    const select = document.querySelector('#bookRoomNum');
    if (!select || !select.value) return 0; // 선택 안 된 경우
    const selectedOption = select.options[select.selectedIndex];
    const price = selectedOption.getAttribute('data-price');
    return price ? parseInt(price, 10) : 0;
}

// 숙박일 계산
function calculateStayDays() {
    const checkIn = document.querySelector('#checkIn').value;
    const checkOut = document.querySelector('#checkOut').value;
    if (!checkIn || !checkOut) return 0;
    const inDate = new Date(checkIn);
    const outDate = new Date(checkOut);
    const diff = (outDate - inDate) / (1000 * 60 * 60 * 24);
    return diff > 0 ? diff : 0;
}

// 요약 박스 업데이트
function updateSummaryBox() {
    // 침대
    const beds = Array.from(document.querySelectorAll('input[name="bed"]:checked')).map(cb => {
        if (cb.value === "extraBed") return "간이 침대";
        if (cb.value === "infantBed") return "유아 침대";
        return "";
    }).filter(Boolean);

    document.querySelector("#bedOption").textContent = isBedTouched
        ? (beds.length > 0 ? beds.join(", ") : "없음")
        : "";

    // 체크인 시간
    const checkInRadio = document.querySelector('input[name="checkInTime"]:checked');
    const checkInLabel = checkInRadio ? document.querySelector(`label[for="${checkInRadio.id}"]`).textContent : "";
    document.querySelector("#checkInOption").textContent = isCheckInTimeTouched ? checkInLabel : "";

    // 인원
    document.querySelector("#totPerson").textContent = pax.adult + pax.children + pax.infant;
}

// 객실명과 가격 표시 함수
function updateRoomInfo() {
    const roomSelect = document.querySelector("#bookRoomNum");
    if (!roomSelect.value) {
        document.querySelector("#selectedRoomNameDisplay").textContent = "선택하세요";
        document.querySelector("#roomPrice").textContent = "0 원";
        return;
    }
    
    const selectedOption = roomSelect.options[roomSelect.selectedIndex];
    const name = selectedOption.getAttribute("data-name");
    const price = selectedOption.getAttribute("data-price");
	console.log("선택 객실:", name, price);

    document.querySelector("#selectedRoomNameDisplay").textContent = name;
    document.querySelector("#roomPrice").textContent = Number(price).toLocaleString() + " 원";
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
