
// 기호를 기준으로 나눠서 현재 프로젝트 위치 가져온 후 sse 주소 호출
const contextPath = "/" + window.location.pathname.split("/")[1];
const eventSource = new EventSource(contextPath + "/sse");

// HTML 요소가 삽입될 부모 요소
const offcanvasBody = document.querySelector(".offcanvas-body");

// 데이터를 성공적으로 받았을 때 실행
eventSource.onmessage = function(event) {
      const notiData = JSON.parse(event.data); // JSON 배열 파싱
	  offcanvasBody.innerHTML = ""; // 기존 요소 삭제

      // DOM에 알림 데이터 동적 삽입
      notiData.forEach(noti => {
        const notiCard = `
			<div class="noti-card d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25">
				<span class="position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
				<div class="noti-img-wrapper ratio ratio-1x1">
					<img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
				</div>
				<div class="noti-text-wrapper d-flex flex-column">
					<div class="d-flex justify-content-between position-relative">
						<div class="d-flex flex-column ps-3 flex-grow-1">
							<span class="text-secondary fs-8">${noti.createdAt}</span>
							<span class="text-semiblack fw-semibold fs-6">${noti.senderId}</span>
						</div>
						<div class="noti-type-wrapper position-absolute end-0">
							<span class="text-secondary fs-8">${noti.typeGroupId}</span>
						</div>
					</div>
					<hr />
					<div class="noti-message-wrapper d-flex ps-3 justify-content-between">
						<span class="text-semiblack fs-7">${noti.message}</span>
						<span class="text-secondary fs-8">27일 전</span>
					</div>
				</div>
			</div>
		`;

		offcanvasBody.insertAdjacentHTML("beforeend", notiCard)
      });
    };

    eventSource.onerror = function(e) {
      console.error("SSE 연결 에러 발생", e);
    };