// SSE를 사용하기 위해 클라이언트에서 사용하는 내장 객체인 EventSource 생성
// 인자는 SSE 요청을 보낼 서버 주소 (서버에 정의된 서블릿의 URL 매핑값과 일치해야함)
const eventSource = new EventSource("/sse");

// 서버에서 데이터를 보내면 이벤트핸들러로 처리
eventSource.onmessage = function(event) {
  const notiData = JSON.parse(event.data); // JSON 배열 파싱
  //output.innerHTML = ""; // 기존 출력 초기화
  
  notiData.forEach(noti => {
    //const msg = document.createElement("p");
    //const date = document.createElement("p");
    //msg.textContent = noti.message;
    //date.textContent = noti.created_at;
    //output.appendChild(msg);
    //output.appendChild(date);
	console.log(noti.createdAt);
	console.log(noti.senderId);
	console.log(noti.message);
	console.log(noti.typeGroupId);
  });
};

// SSE 에러 발생 시 실행되는 예외 처리 핸들러
eventSource.onerror = function(e) {
  console.error("SSE 연결 에러 발생", e);
};