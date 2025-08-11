window.addEventListener("DOMContentLoaded", () => {
    // 인원 정보 관리 객체
    const pax = {
        adult: parseInt(document.querySelector("#adult").value) || 0,
        children: parseInt(document.querySelector("#children").value) || 0,
        infant: parseInt(document.querySelector("#infant").value) || 0
    };

    // flatpickr 인스턴스 선언
    let datePicker = null;

    
    //flatpickr 초기화 함수
     
    function initDatePicker(disabledDates = []) {
        // 기존 인스턴스가 있으면 제거
        if (datePicker) datePicker.destroy();

        datePicker = flatpickr("#dateRange", {
            mode: "range",
            minDate: "today",
            dateFormat: "Y-m-d",
            locale: flatpickr.l10ns.ko,
            disable: disabledDates,
            onChange: handleDateChange
        });
    }

    
     //flatpickr onChange 이벤트 핸들러
     //체크인 / 체크아웃 날짜 설정 및 요약 박스 업데이트
    function handleDateChange(selectedDates, dateStr) {
        const formatDate = (date) => {
            const y = date.getFullYear();
            const m = String(date.getMonth() + 1).padStart(2, '0');
            const d = String(date.getDate()).padStart(2, '0');
            return `${y}-${m}-${d}`;
        };

        if (selectedDates.length === 2) {
            const checkInStr = formatDate(selectedDates[0]);
            const checkOutStr = formatDate(selectedDates[1]);

            document.querySelector("#checkIn").value = checkInStr;
            document.querySelector("#checkOut").value = checkOutStr;

            const summary = document.querySelector("#dateSummary");
            if (summary) {
                summary.style.display = "block";
                summary.textContent = `📅 체크인 : ${checkInStr} ➜ 체크아웃 : ${checkOutStr}`;
            }
        } else {
            document.querySelector("#checkIn").value = "";
            document.querySelector("#checkOut").value = "";

            const summary = document.querySelector("#dateSummary");
            if (summary) {
                summary.style.display = "none";
                summary.textContent = "";
            }
        }

        calculateTotalAmount();
        updateSummaryBox();
    }

    
     //서버에서 예약 불가 날짜를 가져와서 flatpickr 재초기화
     
    function loadDisabledDates(bookRoomNum) {
        fetch(`${CONTEXT_PATH}/getDisabledDates?bookRoomNum=${bookRoomNum}`)
            .then(res => res.json())
            .then(disabledDates => {
                initDatePicker(disabledDates);
            })
            .catch(err => {
                console.error(err);
                // 실패 시 예약 불가 날짜 없이 초기화
                initDatePicker();
            });
    }

    
    //인원 수 UI 갱신
  
    function updateUI() {
        document.querySelector("#adultCount").textContent = pax.adult;
        document.querySelector("#childrenCount").textContent = pax.children;
        document.querySelector("#infantCount").textContent = pax.infant;

        document.querySelector("#totPerson").textContent = pax.adult + pax.children + pax.infant;

        document.querySelector("#adult").value = pax.adult;
        document.querySelector("#children").value = pax.children;
        document.querySelector("#infant").value = pax.infant;
    }

     //인원수 변경 
    window.changeCount = (type, delta) => {
        if (pax.hasOwnProperty(type)) {
            pax[type] = Math.max(0, pax[type] + delta);
            updateUI();
            calculateTotalAmount();
            updateSummaryBox();
        }
    };

    
     //객실 선택시 실행
    function onRoomChange() {
        const bookRoomNum = document.querySelector("#bookRoomNum").value;
        if (bookRoomNum) {
            loadDisabledDates(bookRoomNum);
        } else {
            // 객실 미선택 시 날짜 선택기 기본 초기화
            initDatePicker();
        }

        updateRoomInfo();
        calculateTotalAmount();
        updateSummaryBox();
    }


     //객실 가격 및 이름 정보 업데이트
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

      //총 금액
    function calculateTotalAmount() {
        const pricePerDay = getSelectedRoomPrice();
        const days = calculateStayDays();
        const basicTotal = pricePerDay * days;

        // 추가 인원 요금
        const addAdultCount = Math.max(0, pax.adult - 2);
        const addAdult = addAdultCount * 50000;
        const addChildren = pax.children * 25000;
        const addInfant = pax.infant * 10000;

        // 침대 옵션 추가 금액
        const addBeds = Array.from(document.querySelectorAll('input[name="bed"]:checked'));
        let addExtraBed = 0;
        let addInfantBed = 0;
        addBeds.forEach(e => {
            if (e.value === "extraBed") addExtraBed += 55000;
            if (e.value === "infantBed") addInfantBed += 15000;
        });

        const total = basicTotal + addAdult + addChildren + addInfant + addExtraBed + addInfantBed;

        document.querySelector('#totalAmountValue').value = total;
    }

     //선택한 객실 가격조회
    function getSelectedRoomPrice() {
        const select = document.querySelector('#bookRoomNum');
        if (!select || !select.value) return 0;
        const selectedOption = select.options[select.selectedIndex];
        const price = selectedOption.getAttribute('data-price');
        return price ? parseInt(price, 10) : 0;
    }
   
   //숙박 일 수 계산
    function calculateStayDays() {
        const checkIn = document.querySelector('#checkIn').value;
        const checkOut = document.querySelector('#checkOut').value;
        if (!checkIn || !checkOut) return 0;
        const inDate = new Date(checkIn);
        const outDate = new Date(checkOut);
        const diff = (outDate - inDate) / (1000 * 60 * 60 * 24);
        return diff > 0 ? diff : 0;
    }

    
     //예약 요약 박스 UI 업데이트
   
    function updateSummaryBox() {
        // 침대 옵션 텍스트 변환
        const beds = Array.from(document.querySelectorAll('input[name="bed"]:checked')).map(cb => {
            if (cb.value === "extraBed") return "간이 침대";
            if (cb.value === "infantBed") return "유아 침대";
            return "";
        }).filter(Boolean);

        const bedOption = document.querySelector("#bedOption");
        if (bedOption) {
            bedOption.textContent = beds.length > 0 ? beds.join(", ") : "선택된 옵션이 없습니다";
        }

        // 체크인 시간 옵션
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

        // 총 인원 표시
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

    
     //체크인 / 체크아웃 시간 라디오 변경 이벤트 
    function bindCheckInTimeChange() {
        document.querySelectorAll('input[name="checkInTime"]').forEach(radio => {
            radio.addEventListener('change', (e) => {
                document.querySelector('#selectedCheckInTime').value = e.target.value;
                updateSummaryBox();
            });
        });
    }

    
     //침대 옵션 체크박스 변경 이벤트 바인딩
    
    function bindBedOptionChange() {
        document.querySelectorAll('input[name="bed"]').forEach(cb => {
            cb.addEventListener('change', () => {
                const selectedBeds = Array.from(document.querySelectorAll('input[name="bed"]:checked'))
                    .map(b => b.value);
                document.querySelector('#selectedBed').value = selectedBeds.join(',');
                calculateTotalAmount();
                updateSummaryBox();
            });
        });
    }

   
     
     //예약 폼 제출 전 필수 체크
    function bindFormSubmit() {
        const form = document.querySelector("#bookForm");
        form.addEventListener("submit", (e) => {
            const roomSelect = document.querySelector("#bookRoomNum");
            const checkIn = document.querySelector("#checkIn").value;
            const checkOut = document.querySelector("#checkOut").value;

            let alertMessage = "";
            if (!roomSelect.value) {
                alertMessage = "객실은 선택해 주세요";
            } else if (!checkIn || !checkOut) {
                alertMessage = "체크인 체크아웃 날짜를 선택해 주세요";
            } else if (pax.adult < 1) {
                alertMessage = "성인은 최소 1명 이상이어야 합니다";
            } else if ((pax.adult + pax.children + pax.infant) < 1) {
                alertMessage = "최소 한 명 이상의 투숙 인원을 선택해 주세요";
            }

            if (alertMessage) {
                e.preventDefault();
                showAlertModal(alertMessage);
                return false;
            }
        });
    }

    /**
     * 부트스트랩 모달로 경고 메시지 출력
     * @param {string} message 
     */
    function showAlertModal(message) {
        const modalBody = document.querySelector("#alertModalBody");
        modalBody.textContent = message;

        const alertModal = new bootstrap.Modal(document.querySelector("#alertModal"));
        alertModal.show();
    }

    // 객실 변경 이벤트 등록
    document.querySelector("#bookRoomNum").addEventListener("change", onRoomChange);

    // 초기 flatpickr 초기화 (예약 불가 날짜 없이)
    initDatePicker();

    // 이벤트 바인딩
    bindBedOptionChange();
    bindCheckInTimeChange();
    bindFormSubmit();

    // 초기화 작업
    updateUI();
    updateRoomInfo();
    calculateTotalAmount();
    updateSummaryBox();
});
