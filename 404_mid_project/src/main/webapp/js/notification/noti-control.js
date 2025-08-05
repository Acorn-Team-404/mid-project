
const notiModal = document.getElementById("offcanvasWithBothOptions");

notiModal.addEventListener("show.bs.offcanvas", () => {
	if (!window.eventSourceInitialized) { // 이미 연결됐는지 확인(중복 연결 방지)
		const offcanvasBody = document.querySelector(".offcanvas-body");
		if (offcanvasBody) offcanvasBody.innerHTML = ""; // 알림 목록 초기화

		initializeSSE();
	} else {
		console.log("⚠️ 이미 SSE 연결 중 - 새 연결 생략");
	}
});

notiModal.addEventListener("hidden.bs.offcanvas", () => {
    if (window.eventSource) {
        window.eventSource.close();
        window.eventSource = null;                 // ✅ 이거 빠지면 메모리에 남아있음
        window.eventSourceInitialized = false;     // ✅ 이거 없으면 다시 안 연결됨
        console.log("🔌 SSE 연결 종료됨");
    }
});





const contextPath = "/" + window.location.pathname.split("/")[1];

async function setRead(notiCard) {
  const notiNum = notiCard.dataset.notiNum;
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


document.querySelector(".offcanvas-body").addEventListener("click", function(e) {
	const notiCard = e.target.closest(".noti-card");
	if (!notiCard) return;

	setRead(notiCard);
});


