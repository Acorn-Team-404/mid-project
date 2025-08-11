

const servletPath = window.contextPath + "/pay/PaymentsServlet";
//console.log("successUrl", window.location.origin + servletPath);
//console.log("failUrl", window.location.origin + servletPath);
//console.log("contextPath is", contextPath);


let selectedPaymentMethod = null;

// 선택한 제품(숙소)가격
const amount = {
	currency: "KRW",
	value: Number(bookingInfo.amount), // 예약테이블에 저장된 가격
};

function selectPaymentMethod (method) {
	if (selectedPaymentMethod != null) {
		document.getElementById(selectedPaymentMethod).style.backgroundColor = "#ffffff";
	}

	selectedPaymentMethod = method;

	document.getElementById(selectedPaymentMethod).style.backgroundColor = "rgb(229 239 255)";
}

// ------  SDK 초기화 ------
// TODO: clientKey는 개발자센터의 API 개별 연동 키 > 결제창 연동에 사용하려할 MID > 클라이언트 키로 바꾸세요.
// TODO: server.js 의 secretKey 또한 결제위젯 연동 키가 아닌 API 개별 연동 키의 시크릿 키로 변경ㄱㄱ.
// TODO: 구매자의 고유 아이디를 불러와서 customerKey로 설정하세요. 이메일・전화번호와 같이 유추가 가능한 값은 안전하지 않음.
// @docs https://docs.tosspayments.com/sdk/v2/js#토스페이먼츠-초기화
const clientKey = "test_ck_Poxy1XQL8Rlj7jnKNRXXV7nO5Wml"; //임채호 키임
const customerKey = generateRandomString();
const tossPayments = TossPayments(clientKey);
// 회원 결제
// @docs https://docs.tosspayments.com/sdk/v2/js#tosspaymentspayment
const payment = tossPayments.payment({customerKey,
});


// ------ 결제하기 버튼 누르면 결제창 띄우기 ------
// @docs https://docs.tosspayments.com/sdk/v2/js#paymentrequestpayment;
async function requestPayment() {
	// 결제를 요청하기 전에 orderId, amount를 서버에 저장해야함
	// 결제 과정에서 악의적으로 결제 금액이 바뀌는 것을 확인하는 용도
	switch (selectedPaymentMethod) {
		case "CARD":
			await payment.requestPayment({
				method: "CARD", // 카드 및 간편결제
				amount,
				orderId: generateRandomString(),
				orderName: bookingInfo.stayName + "/" + bookingInfo.roomName + "/ 총인원 :" + bookingInfo.totalPax, // 숙소명+ 방번호 + 인원수 + ~~@
				successUrl:  window.location.origin + servletPath,
				failUrl:  window.location.origin + servletPath,
				customerEmail: "limchaehozzang@gmail.com",
				customerName: bookingInfo.userName,
				// 가상계좌 안내, 퀵계좌이체 휴대폰 번호 자동 완성에 사용되는 값.
				// customerMobilePhone: "01012341234",
				card: {
					useEscrow: false,
					flowMode: "DEFAULT",
					useCardPoint: false,
					useAppCardOnly: false,
				},
			});break;
			
			
		case "TRANSFER":
			await payment.requestPayment({
				method: "TRANSFER", // 계좌이체 결제
				amount,
				orderId: generateRandomString(),
				orderName: bookingInfo.stayName + "/" + bookingInfo.roomName + "/ 총인원 :" + bookingInfo.totalPax,
				successUrl:  window.location.origin + servletPath,
				failUrl:  window.location.origin + servletPath,
				customerEmail: "limchaehozzang@gmail.com",
				customerName: bookingInfo.userName,
				// 가상계좌 안내, 퀵계좌이체 휴대폰 번호 자동 완성에 사용되는 값.
				// customerMobilePhone: "01012341234",
				transfer: {
					cashReceipt: {
						type: "소득공제",
					},
					useEscrow: false,
				},
			});break;	
			
		case "MOBILE_PHONE":
			await payment.requestPayment({
				method: "MOBILE_PHONE", // 휴대폰 결제
				amount,
				orderId: generateRandomString(),
				orderName: bookingInfo.stayName + "/" + bookingInfo.roomName + "/ 총인원 :" + bookingInfo.totalPax,
				successUrl:  window.location.origin + servletPath,
				failUrl:  window.location.origin + servletPath,
				customerEmail: "limchaehozzang@gmail.com",
				customerName: bookingInfo.userName,
				// 가상계좌 안내, 퀵계좌이체 휴대폰 번호 자동 완성에 사용되는 값
				// customerMobilePhone: "01012341234",
			});break;
		
		
	};
}

function generateRandomString() {
	return window.btoa(Math.random()).slice(0, 20);
}

// 전역 scope 등록
window.selectPaymentMethod = selectPaymentMethod;
window.requestPayment = requestPayment;

for (let i = 1; i < 6; i++) {
	console.log(i);
}
