
// 전역변수 중복 선언 방지 (없다면 선언)

if (typeof window.eventSourceInitialized === "undefined") {
	window.eventSourceInitialized = false; // SSE 연결 여부를 판단하는 변수. false = 연결 x
}
if (typeof window.eventSource === "undefined") {
	window.eventSource = null; // 실제 SSE 객체를 담을 변수
}

// DOM이 완성된 후 SSE 실행
document.addEventListener("DOMContentLoaded", () => {
  initializeSSE();
});

function initializeSSE() {

	if (window.eventSourceInitialized) {
		console.log("🔁 SSE 이미 연결됨 - 중복 연결 방지");
		return;
	}

	// 프로젝트 경로를 찾고 SSE 연결
	const contextPath = "/" + window.location.pathname.split("/")[1];
	window.eventSource = new EventSource(contextPath + "/sse");
	window.eventSourceInitialized = true;

	// 요소를 삽입할 부모요소
	const offcanvasBody = document.querySelector(".offcanvas-body");

	// 메세지를 받았을 때 실행
	window.eventSource.onmessage = function(event) {
		console.log("✅ SSE 연결 시작됨");
		const notiData = JSON.parse(event.data); // 파싱
		
		// offcanvasBody.innerHTML = ""; // 기존 알림 제거 (중복 방지)
		
		let notiCountNum = document.querySelector(".noti-btn-count") // 안읽은 알림 수 표시할 요소
		
		notiData.forEach(noti => {
			
			// 안읽은 알림이 없다면
			if(notiData[0].readCount == 0) {
				notiCountNum.classList.add("d-none") // 요소 안보이게
			} else { // 있으면
				notiCountNum.classList.remove("d-none") // 보이게 하고
				if(notiData[0].readCount > 9) {
					notiCountNum.innerText = "9+"
				} else {
					notiCountNum.innerText = `${notiData[0].readCount}` // 안읽은 알림 수 표시
				}
			}
			
			let notiCard = "";
			
			// 예약 타입 카드
			if(noti.typeCode == 10) {
				notiCard = `
					<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25" data-noti-num="${noti.notiNum}" onclick="location.href='${contextPath}/mypage/detail?bookNum=${noti.bookNum}'">
						<span class="read-dot ${noti.readCode === 11 ? 'd-none' : ''} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
						<div class="noti-img-wrapper ratio ratio-1x1">
							<img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
						</div>
						<div class="noti-text-wrapper d-flex flex-column">
							<div class="d-flex justify-content-between position-relative">
								<div class="d-flex flex-column ps-3 flex-grow-1">
									<span class="text-secondary fs-8">${noti.bookCheckIn + " ~ " + noti.bookCheckOut}</span>
									<span class="text-semiblack fw-semibold fs-6">${noti.stayName}</span>
								</div>
								<div class="noti-type-wrapper position-absolute end-0">
									<span class="text-secondary fs-8">${noti.type}</span>
								</div>
							</div>
							<hr />
							<div class="noti-message-wrapper d-flex ps-3 justify-content-between">
								<span class="text-semiblack fs-7">${noti.message}</span>
								<span class="text-secondary fs-8">${noti.daysAgo}</span>
							</div>
						</div>
					</div>
				`;
			}


			// 댓글 타입 카드
			if(noti.typeCode == 20) {
				notiCard = `
					<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25" data-noti-num="${noti.notiNum}">
						<span class="read-dot ${noti.readCode === 11 ? 'd-none' : ''} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
						<div class="noti-img-wrapper ratio ratio-1x1">
							<img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
						</div>
						<div class="noti-text-wrapper d-flex flex-column">
							<div class="d-flex justify-content-between position-relative">
								<div class="d-flex flex-column ps-3 flex-grow-1">
									<span class="text-secondary fs-8">${noti.createdAt}</span>
									<span class="text-semiblack fw-semibold fs-6">${noti.commentWriter}</span>
								</div>
								<div class="noti-type-wrapper position-absolute end-0">
									<span class="text-secondary fs-8">${noti.type}</span>
								</div>
							</div>
							<hr />
							<div class="noti-message-wrapper d-flex ps-3 justify-content-between">
								<span class="text-semiblack fs-7">${noti.commentContent}</span>
								<span class="text-secondary fs-8">${noti.daysAgo}</span>
							</div>
						</div>
					</div>
				`;
			}
			
			
			
			// 리뷰 타입 카드(미완성)
			if(noti.typeCode == 30) {
				notiCard = `
					<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25" data-noti-num="${noti.notiNum}">
						<span class="read-dot ${noti.readCode === 11 ? 'd-none' : ''} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
						<div class="noti-img-wrapper ratio ratio-1x1">
							<img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
						</div>
						<div class="noti-text-wrapper d-flex flex-column">
							<div class="d-flex justify-content-between position-relative">
								<div class="d-flex flex-column ps-3 flex-grow-1">
									<span class="text-secondary fs-8">${noti.createdAt}</span>
									<span class="text-semiblack fw-semibold fs-6">${noti.commentWriter}</span>
								</div>
								<div class="noti-type-wrapper position-absolute end-0">
									<span class="text-secondary fs-8">${noti.type}</span>
								</div>
							</div>
							<hr />
							<div class="noti-message-wrapper d-flex ps-3 justify-content-between">
								<span class="text-semiblack fs-7">${noti.commentContent}</span>
								<span class="text-secondary fs-8">${noti.daysAgo}</span>
							</div>
						</div>
					</div>
				`;
			}
			
			
			// 문의 타입 카드(미완성)
			if(noti.typeCode == 40) {
				notiCard = `
					<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25" data-noti-num="${noti.notiNum}" onclick="location.href='${contextPath}/inquiry/list.jsp'">
						<span class="read-dot ${noti.readCode === 11 ? 'd-none' : ''} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
						<div class="noti-img-wrapper ratio ratio-1x1">
							<img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
						</div>
						<div class="noti-text-wrapper d-flex flex-column">
							<div class="d-flex justify-content-between position-relative">
								<div class="d-flex flex-column ps-3 flex-grow-1">
									<span class="text-secondary fs-8">${noti.createdAt}</span>
									<span class="text-semiblack fw-semibold fs-6">${noti.inqTitle}</span>
								</div>
								<div class="noti-type-wrapper position-absolute end-0">
									<span class="text-secondary fs-8">${noti.type}</span>
								</div>
							</div>
							<hr />
							<div class="noti-message-wrapper d-flex ps-3 justify-content-between">
								<span class="text-semiblack fs-7">${noti.inqContent}</span>
								<span class="text-secondary fs-8">${noti.daysAgo}</span>
							</div>
						</div>
					</div>
				`;
			}
						
						
						
						
						
			
			
			// 카드 삽입
			offcanvasBody.insertAdjacentHTML("afterbegin", notiCard);
			
			
		});
	};

	window.eventSource.onerror = function(e) {
		console.error("❌ SSE 연결 에러 발생", e);
	};
	

}