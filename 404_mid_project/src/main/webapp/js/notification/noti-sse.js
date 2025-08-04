
if (typeof window.eventSourceInitialized === "undefined") {
	window.eventSourceInitialized = false;
}
if (typeof window.eventSource === "undefined") {
	window.eventSource = null;
}

function initializeSSE() {
	if (window.eventSourceInitialized) {
		console.log("🔁 SSE 이미 연결됨 - 중복 연결 방지");
		return;
	}

	const contextPath = "/" + window.location.pathname.split("/")[1];
	window.eventSource = new EventSource(contextPath + "/sse");
	window.eventSourceInitialized = true;

	const offcanvasBody = document.querySelector(".offcanvas-body");

	window.eventSource.onmessage = function(event) {
		console.log("✅ SSE 연결 시작됨");
		const notiData = JSON.parse(event.data);
		
		// offcanvasBody.innerHTML = ""; // 기존 알림 제거 (중복 방지)

		notiData.forEach(noti => {
			let notiCard = "";

			if(noti.typeCode == 10) {
				notiCard = `
					<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25">
						<span class="position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
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


			
			if(noti.typeCode == 20) {
				notiCard = `
					<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25">
						<span class="position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
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

			offcanvasBody.insertAdjacentHTML("afterbegin", notiCard);
		});
	};

	window.eventSource.onerror = function(e) {
		console.error("❌ SSE 연결 에러 발생", e);
	};
}