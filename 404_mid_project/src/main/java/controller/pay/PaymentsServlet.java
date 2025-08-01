package controller.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.json.simple.JSONObject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pay/PaymentsServlet")
public class PaymentsServlet extends HttpServlet {

	// 임채호 토스 페이먼츠 시크릿키
	private static final String SECRET_KEY = "test_sk_Z61JOxRQVENQmw46ywdarW0X9bAq";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 토스 서버에 쏴줄 파라미터들 수집하기
		String paymentKey = request.getParameter("paymentKey");
		String orderId = request.getParameter("orderId");
		String amount = request.getParameter("amount");

		String credentials = SECRET_KEY + ":";
		// 인증헤더 구성 (Base64를 인코딩하려면 바이트 배열이여햐 하기 떄문에 byte배열로 인코딩 후 Base64 문자열로 인코딩)
		String encodedAuth = Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));
		// HTTP의 기본 인증 방식임 Authorization: Basic + Base64(아이디:비밀번호)
		String authorization = "Basic " + encodedAuth;

		// 토스 서버에 API 요청 준비하기
		// url = 토스 결제 승인 API
		URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// API 서버에 POST방식으로 요청 보내기
		// 데이터 전송이 목적이기 때문에 토스의 결제 승인 API는 POST만 지원함
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", authorization);
		// 서버에게 보내는 데이터가 JSON 형식임을 명시해서 서버가 body에 있는 데이터를 JSON으로 해석할 수 있게 유도
		conn.setRequestProperty("Content-Type", "application/json");
		// body에 데이터 담을 수 있도록 설정
		conn.setDoOutput(true);

		// JSON 데이터 작성하기
		// json 파라미터 안에 방 예약정보 가져와서 넣기
		JSONObject json = new JSONObject();
		json.put("paymentKey", paymentKey);
		json.put("orderId", orderId);
		json.put("amount", Integer.parseInt(amount));
		
		// 아웃풋스트림 객체로 전송하기
		try (OutputStream os = conn.getOutputStream()) {
			os.write(json.toString().getBytes("UTF-8"));
		}

		// 인풋스트림 객체로 전송하기
		int responseCode = conn.getResponseCode();
		InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder result = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}

		// 결제 승인 API 호출되는지 확인하기
		if (responseCode == 200) {
			System.out.println("결제 승인 성공");
			System.out.println("응답: " + result.toString());

		} else {
			System.out.println("결제 승인 실패");
			System.out.println("응답 코드: " + responseCode);
			System.out.println("에러 메시지: " + result.toString());
		}

		// 결제 완료 페이지 (result.jsp 같은 거)로 리디렉션 해줘야함
		response.sendRedirect(request.getContextPath() + "/pay/pay-result.jsp");
		return;

	}

}
