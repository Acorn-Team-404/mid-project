/* 채린 영역 -start */
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
	
	// 체크인 날짜와 체크아웃 날짜 이전 이후 선택 알람 처리
	document.querySelector("#checkIn").addEventListener("change", (e)=>{
		const cIn = document.querySelector("#checkIn").value.trim();
		const cOut = document.querySelector("#checkOut").value.trim();
		if (cIn && cOut) {
			const checkIn = new Date(cIn);
		    const checkOut = new Date(cOut);

		    if (checkIn > checkOut) {
				e.preventDefault();
		        showAlertModal("체크인 날짜는 체크아웃 날짜 이전으로 선택해 주세요");
				document.querySelector("#checkIn").value = "";
				document.querySelector("#checkIn").focus();
		        return;
			}
		}
	});
	document.querySelector("#checkOut").addEventListener("change", (e)=>{
		const cIn = document.querySelector("#checkIn").value.trim();
		const cOut = document.querySelector("#checkOut").value.trim();
		if (cIn && cOut) {
			const checkIn = new Date(cIn);
			const checkOut = new Date(cOut);

			if (checkIn > checkOut) {
				e.preventDefault();
				showAlertModal("체크아웃 날짜는 체크인 날짜 이후로 선택해 주세요");
				document.querySelector("#checkOut").value = "";
				document.querySelector("#checkOut").focus();
				return;
			}
		}
	});

	// 총 금액 처리
    function calculateTotalAmount() {
        const pricePerDay = getSelectedRoomPrice();
        const days = calculateStayDays();
        const total = pricePerDay * days;
        document.querySelector('#totalAmountValue').value = total;
    }
	
	// 객실 금액 조회
    function getSelectedRoomPrice() {
        const select = document.querySelector('#bookRoomNum');
        if (!select || !select.value) return 0;
        const selectedOption = select.options[select.selectedIndex];
        const price = selectedOption.getAttribute('data-price');
        return price ? parseInt(price, 10) : 0;
    }
	
	// 모달창 띄우기
	function showAlertModal(message){
	    const modalBody = document.querySelector("#alertModalBody");
	    modalBody.textContent = message;
	    const modal = new bootstrap.Modal(document.querySelector("#alertModal"));
	    modal.show();
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

	// 옵션 선택 후 사용자에게 보여주는 박스
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
/* 채린 영역 -end */