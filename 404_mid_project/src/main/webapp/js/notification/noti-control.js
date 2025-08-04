const contextPath = "/" + window.location.pathname.split("/")[1];

async function setRead(notiCard) {
  const notiNum = notiCard.dataset.notiNum;
  const readDot = document.querySelector(".read-dot")
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

	console.log("클릭은 됨")
	if (!notiCard) return;

	setRead(notiCard);
});


/*document.querySelectorAll(".noti-card").forEach((notiCard) => {
	notiCard.addEventListener("click", () => {
		console.log("클릭은 됨")
		setRead(notiCard);
	})
})
*/
