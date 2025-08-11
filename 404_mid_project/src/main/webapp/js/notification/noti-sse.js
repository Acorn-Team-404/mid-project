

// 전역변수 중복 선언 방지 (없다면 선언)
if (typeof window.eventSourceInitialized === "undefined") {
  window.eventSourceInitialized = false; // SSE 연결 여부
}
if (typeof window.eventSource === "undefined") {
  window.eventSource = null; // 실제 SSE 객체
}

// DOM 완성 후 SSE 실행 (오프캔버스 열릴 때만 연결하고 싶다면 이 줄은 빼고 show.bs.offcanvas에서만 initializeSSE 호출)
document.addEventListener("DOMContentLoaded", () => {
  initializeSSE();
});

// SSE를 호출하는 함수
function initializeSSE() {
  if (window.eventSourceInitialized) {
    console.log("🔁 SSE 이미 연결됨 - 중복 연결 방지");
    return; // 이미 연결되어 있다면 종료
  }

  const contextPath = "/" + window.location.pathname.split("/")[1];
  const offcanvasBody = document.querySelector(".offcanvas-body"); // 알림 카드를 삽입할 부모요소
  if (!offcanvasBody) {
    console.warn("offcanvas-body 요소를 찾지 못함");
  }

  // 서버가 보내는 실시간 이벤트를 받을 내장 객체
  window.eventSource = new EventSource(contextPath + "/sse");
  window.eventSourceInitialized = true; // 연결된 상태로 변경

  
  // 읽지 않은 알림 수를 가져오는 이벤트리스너
  window.eventSource.addEventListener("count", (event) => {
    let data;
    try {
      data = JSON.parse(event.data);
    } catch (e) {
      console.error("count JSON 파싱 실패:", e, event.data);
      return;
    }
    const count = data.readCount ?? 0; // null이나 undefined라면 0으로 사용
    updateBadge(count);
  });

  
  
  // 알림 카드를 출력하는 이벤트리스너
  window.eventSource.addEventListener("noti", (event) => {
    let notiData;
    try {
      notiData = JSON.parse(event.data);
    } catch (e) {
      console.error("noti JSON 파싱 실패:", e, event.data);
      return;
    }
	// 1개 알림일 경우 배열이 아닌 객체로만 오기 때문에 배열이 아닐 경우 배열로 변환
    const arr = Array.isArray(notiData) ? notiData : [notiData];

    arr.forEach((noti) => {
      // 안읽은 알림 수 갱신
      const rc = Number.isFinite(noti.readCount) ? noti.readCount : 0;
      updateBadge(rc);

      // 알림카드 렌더링
      const html = renderNotiCard(noti, contextPath);
      if (html && offcanvasBody) {
        offcanvasBody.insertAdjacentHTML("afterbegin", html);
      }
    });
  });

  
  window.eventSource.onerror = function (e) {
    console.error("❌ SSE 연결 에러 발생", e);
  };
}


// 안읽은 알림 수를 갱신하는 함수
function updateBadge(count) {
  const notiCountNum = document.querySelector(".noti-btn-count");
  if (!notiCountNum) return;
  const c = Number(count) || 0; // 숫자로 변환하고, 변환이 실패하면 0을 사용
  if (c === 0) {
    notiCountNum.classList.add("d-none");
  } else {
    notiCountNum.classList.remove("d-none");
    notiCountNum.innerText = c > 9 ? "+9" : String(c);
  }
}


// 알림 카드 템플릿 (타입에 따라서 분기)
function renderNotiCard(noti, contextPath) {
  if (!noti || typeof noti !== "object") return "";

  const typeCode = Number(noti.typeCode);
  let notiCard = "";

  // 예약 타입
  //if (typeCode === 10 || typeCode === 11) { 일단 보류
  if (typeCode === 10) {
    notiCard = `
      <div class="noti-card shadow-sm d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25"
           data-noti-num="${noti.notiNum ?? ""}"
           onclick="location.href='${contextPath}/booking/confirm?bookNum=${noti.bookNum ?? ""}'">
        <span class="read-dot ${Number(noti.readCode) === 11 ? "d-none" : ""} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
        <div class="noti-img-wrapper ratio ratio-1x1">
          <img class="img-fluid object-fit-cover rounded-3" src="${contextPath}/show.img?imageName=${encodeURIComponent(noti.imageName ?? "")}" alt="" />
        </div>
        <div class="noti-text-wrapper d-flex flex-column">
          <div class="d-flex justify-content-between position-relative">
            <div class="d-flex flex-column ps-3 flex-grow-1">
              <span class="text-secondary fs-8">${(noti.bookCheckIn ?? "") + " ~ " + (noti.bookCheckOut ?? "")}</span>
              <span class="text-semiblack fw-semibold fs-6" style="max-width: 140px;">${noti.stayName ?? ""}</span>
            </div>
            <div class="noti-type-wrapper position-absolute end-0">
              <span class="text-secondary fs-8">${noti.type ?? ""}</span>
            </div>
          </div>
          <hr />
          <div class="noti-message-wrapper d-flex ps-3 justify-content-between">
            <span class="text-semiblack fs-7" style="max-width: 130px;">${noti.message ?? ""}</span>
            <span class="text-secondary fs-8">${noti.daysAgo ?? ""}</span>
          </div>
        </div>
      </div>
    `;
  }

  // 댓글 타입
  if (typeCode === 20) {
    notiCard = `
      <div class="noti-card shadow-sm d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25"
           data-noti-num="${noti.notiNum ?? ""}"
           onclick="location.href='${contextPath}/post/view.jsp?num=${noti.commentParentNum ?? ""}'">
        <span class="read-dot ${Number(noti.readCode) === 11 ? "d-none" : ""} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
        <div class="noti-img-wrapper ratio ratio-1x1">
          ${
            noti.imageName
              ? `<img class="img-fluid object-fit-cover rounded-3" src="${contextPath}/show.img?imageName=${encodeURIComponent(noti.imageName)}" alt="" />`
              : `<div class="d-flex justify-content-center align-items-center bg-light rounded-3 overflow-hidden" style="width: 80px; height: 80px;">
                   <i class="bi bi-person-fill" style="font-size:3rem;"></i>
                 </div>`
          }
        </div>
        <div class="noti-text-wrapper d-flex flex-column">
          <div class="d-flex justify-content-between position-relative">
            <div class="d-flex flex-column ps-3 flex-grow-1">
              <span class="text-secondary fs-8">${noti.createdAt ?? ""}</span>
              <span class="text-semiblack fw-semibold fs-6" style="max-width: 140px;">${noti.commentWriter ?? ""}</span>
            </div>
            <div class="noti-type-wrapper position-absolute end-0">
              <span class="text-secondary fs-8">${noti.type ?? ""}</span>
            </div>
          </div>
          <hr />
          <div class="noti-message-wrapper d-flex ps-3 justify-content-between">
            <span class="text-semiblack fs-7" style="max-width: 130px;">${noti.commentContent ?? ""}</span>
            <span class="text-secondary fs-8">${noti.daysAgo ?? ""}</span>
          </div>
        </div>
      </div>
    `;
  }

  // 리뷰 타입(미완)
  if (typeCode === 30) {
    notiCard = `
      <div class="noti-card shadow-sm d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25"
           data-noti-num="${noti.notiNum ?? ""}">
        <span class="read-dot ${Number(noti.readCode) === 11 ? "d-none" : ""} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
        <div class="noti-img-wrapper ratio ratio-1x1">
          <img class="img-fluid object-fit-cover rounded-3" src="https://picsum.photos/id/237/200/300" alt="" />
        </div>
        <div class="noti-text-wrapper d-flex flex-column">
          <div class="d-flex justify-content-between position-relative">
            <div class="d-flex flex-column ps-3 flex-grow-1">
              <span class="text-secondary fs-8">${noti.createdAt ?? ""}</span>
              <span class="text-semiblack fw-semibold fs-6" style="max-width: 140px;">${noti.commentWriter ?? ""}</span>
            </div>
            <div class="noti-type-wrapper position-absolute end-0">
              <span class="text-secondary fs-8">${noti.type ?? ""}</span>
            </div>
          </div>
          <hr />
          <div class="noti-message-wrapper d-flex ps-3 justify-content-between">
            <span class="text-semiblack fs-7" style="max-width: 130px;">${noti.commentContent ?? ""}</span>
            <span class="text-secondary fs-8">${noti.daysAgo ?? ""}</span>
          </div>
        </div>
      </div>
    `;
  }

  // 문의 타입(미완)
  if (typeCode === 40) {
    notiCard = `
      <div class="noti-card shadow-sm d-flex position-relative p-3 m-3 bg-secondary bg-opacity-25"
           data-noti-num="${noti.notiNum ?? ""}"
           onclick="location.href='${contextPath}/inquiry/list.jsp'">
        <span class="read-dot ${Number(noti.readCode) === 11 ? "d-none" : ""} position-absolute top-0 end-0 d-inline-block rounded-circle bg-semired" style="width: 11px; height: 11px;"></span>
        <div class="noti-img-wrapper ratio ratio-1x1">
          <div class="d-flex justify-content-center align-items-center bg-light rounded-3 overflow-hidden" style="width: 80px; height: 80px;">
            <i class="bi bi-person-fill" style="font-size:3rem;"></i>
          </div>
        </div>
        <div class="noti-text-wrapper d-flex flex-column">
          <div class="d-flex justify-content-between position-relative">
            <div class="d-flex flex-column ps-3 flex-grow-1">
              <span class="text-secondary fs-8">${noti.createdAt ?? ""}</span>
              <span class="text-truncate text-semiblack fw-semibold fs-6" style="max-width: 140px;">${noti.inqTitle ?? ""}</span>
            </div>
            <div class="noti-type-wrapper position-absolute end-0">
              <span class="text-secondary fs-8">${noti.type ?? ""}</span>
            </div>
          </div>
          <hr />
          <div class="noti-message-wrapper d-flex ps-3 justify-content-between">
            <span class="text-truncate text-semiblack fs-7" style="max-width: 130px;">${noti.inqContent ?? ""}</span>
            <span class="text-secondary fs-8">${noti.daysAgo ?? ""}</span>
          </div>
        </div>
      </div>
    `;
  }

  return notiCard;
}
