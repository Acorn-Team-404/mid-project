
const notiModal = document.getElementById("offcanvasWithBothOptions");

// offcanvas가 열릴 때
notiModal.addEventListener("show.bs.offcanvas", () => {
	if (!window.eventSourceInitialized) { // 이미 연결됐는지 확인(중복 연결 방지)
		const offcanvasBody = document.querySelector(".offcanvas-body");
		if (offcanvasBody) offcanvasBody.innerHTML = ""; // 알림 목록 초기화

		initializeSSE(); // SSE 실행
	} else {
		console.log("⚠️ 이미 SSE 연결 중 - 새 연결 생략");
	}
});

// offcanvas가 닫힐 때
notiModal.addEventListener("hidden.bs.offcanvas", () => {
    if (window.eventSource) { // SSE 객체가 있다면
        window.eventSource.close(); // 연결 종료
        window.eventSource = null; // 가비지 컬렉터가 SSE 객체를 수거하도록 설정
        window.eventSourceInitialized = false; // 다시 연결할 수 있도록 값 변경
        console.log("🔌 SSE 연결 종료됨");
    }
});




// 읽음 처리 함수
const contextPath = "/" + window.location.pathname.split("/")[1];
async function setRead(notiCard) {
  const notiNum = notiCard.dataset.notiNum; // dataset에 저장한 notiNum 가져오기
  const readDot = notiCard.querySelector(".read-dot")
  try {
    const response = await fetch(contextPath + '/setRead.noti', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: 'notiNum=' + encodeURIComponent(notiNum),
    });
	if (!response.ok) {
	        throw new Error(`HTTP 오류! 상태코드: ${response.status}`);
	}
	readDot.classList.add("d-none") 
  } catch (error) {
    console.error('❌ 알림 읽음 처리 실패:', error);
    alert('알림 읽음 처리 중 오류가 발생했습니다.');
  }
}

// 읽음 처리 이벤트핸들러(이미 읽은 알림이라면 return)
document.querySelector(".offcanvas-body").addEventListener("click", function(e) {
	const notiCard = e.target.closest(".noti-card");
	const readDot = notiCard.querySelector(".read-dot");
	if(!notiCard || readDot.classList.contains("d-none")) {
		return;
	} else {
		setRead(notiCard);
	}
});


