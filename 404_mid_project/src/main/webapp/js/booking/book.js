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
	
	// 체크인 - 체크아웃 날짜
	flatpickr("#dateRange", {
	    mode: "range",
	    minDate: "today",
	    dateFormat: "Y-m-d",
	    locale: flatpickr.l10ns.ko,
		onChange: function (selectedDates, dateStr) {
		    if (selectedDates.length === 2) {
		        const formatDate = (date) => {
		            const year = date.getFullYear();
		            const month = String(date.getMonth() + 1).padStart(2, '0');
		            const day = String(date.getDate()).padStart(2, '0');
		            return `${year}-${month}-${day}`;
		        };

		        const checkInStr = formatDate(selectedDates[0]);
		        const checkOutStr = formatDate(selectedDates[1]);

		        document.querySelector("#checkIn").value = checkInStr;
		        document.querySelector("#checkOut").value = checkOutStr;

		        const summary = document.querySelector("#dateSummary");
		        if (summary) {
		            summary.style.display = "block";
		            summary.textContent = `📅 체크인 : ${checkInStr} ➜ 체크아웃 : ${checkOutStr}`;
		        }

		        calculateTotalAmount();
		        updateSummaryBox();
		    } else {
		        document.querySelector("#checkIn").value = "";
		        document.querySelector("#checkOut").value = "";
		        const summary = document.querySelector("#dateSummary");
		        if (summary) {
		            summary.style.display = "none";
		            summary.textContent = "";
		        }
		        calculateTotalAmount();
		        updateSummaryBox();
		    }
		}
	});

    // 침대 옵션 처리
    document.querySelectorAll('input[name="bed"]').forEach(cb => {
        cb.addEventListener('change', () => {
            const selectedBeds = Array.from(document.querySelectorAll('input[name="bed"]:checked'))
                .map(b => b.value);
            document.querySelector('#selectedBed').value = selectedBeds.join(',');
			calculateTotalAmount();
            updateSummaryBox();
        });
    });

    // 체크인 시간 처리
    document.querySelectorAll('input[name="checkInTime"]').forEach(radio => {
	    radio.addEventListener('change', (e) => {
	        document.querySelector('#selectedCheckInTime').value = e.target.value;
	
	        updateSummaryBox();
	    });
	});
	
	// 총 금액 처리
    function calculateTotalAmount() {
        const pricePerDay = getSelectedRoomPrice();
        const days = calculateStayDays();
        const basicTotal = pricePerDay * days;
		
		// 인원 추가금
		// 성인 추가 인원 금액 계산 (기본 2명은 제외)
		const addAdultCount = Math.max(0, pax.adult-2);
		const addAdult = addAdultCount*50000;
		
		// 어린이 추가 인원 금액 계산
		const addChildren = pax.children*25000;
		
		// 유아 추가 인원 금액 계산
		const addInfant = pax.infant*10000;
		
		// 침대 추가 금액 계산		
		const addBeds = Array.from(document.querySelectorAll('input[name="bed"]:checked'));
		let addExtraBed = 0;
		let addInfantBed = 0;

		addBeds.forEach(e => {
		    if(e.value === "extraBed") addExtraBed += 55000;
		    if(e.value === "infantBed") addInfantBed += 15000;
		});


		const total = basicTotal + addAdult + addChildren + addInfant + addExtraBed + addInfantBed;
		
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
		    const labelEl = document.querySelector(`label[for="${checkInRadio.id}"]`);
		    if (labelEl) checkInLabel = labelEl.textContent.trim();
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

        document.querySelector("#selectedRoomNameDisplay").textContent = name;
        document.querySelector("#roomPrice").textContent = Number(price).toLocaleString() + "원";
    }

    // 이벤트 바인딩
    document.querySelector("#bookRoomNum").addEventListener("change", () => {
        updateRoomInfo();
        calculateTotalAmount();
        updateSummaryBox();
    });
	
	// 필수 옵션 선택 안 했을 때 모달 처리
	const form = document.querySelector("#bookForm");
	form.addEventListener("submit", (e)=>{
		// 필수 옵션 항목들 (객실, 체크인과 체크아웃 날짜, 인원)	
		const roomSelect = document.querySelector("#bookRoomNum");
		const checkIn = document.querySelector("#checkIn").value;
		const checkOut = document.querySelector("#checkOut").value;
		const totalPersons = pax.adult + pax.children + pax.infant;

		let alertMessage = "";
		if(!roomSelect.value){
			alertMessage = "객실은 선택해 주세요";
		} else if(!checkIn || !checkOut){
			alertMessage = "체크인 체크아웃 날짜를 선택해 주세요"
		} else if(pax.adult < 1) {
		    alertMessage = "성인은 최소 1명 이상이어야 합니다";
		} else if((pax.adult + pax.children + pax.infant) < 1) {
		    alertMessage = "최소 한 명 이상의 투숙 인원을 선택해 주세요";
		}
		
		if(alertMessage){
			e.preventDefault();
			showAlertModal(alertMessage);
			return false;
		}

	});

	// 모달 창 띄우기
	function showAlertModal(message) {
		const modalBody = document.querySelector("#alertModalBody");
		modalBody.textContent = message;
		
		const alertModal = new bootstrap.Modal(document.querySelector("#alertModal"))
		alertModal.show();
	}

    // 초기 실행
    updateUI();
    updateRoomInfo();
    calculateTotalAmount();
    updateSummaryBox();
});
/* 채린 영역 -end */