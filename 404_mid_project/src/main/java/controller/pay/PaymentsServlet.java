package controller.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.book.BookDao;
import model.book.BookDto;
import model.pay.PaymentDao;
import model.pay.PaymentDto;
import model.util.DBConnector;

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
		
		boolean paymentSuccess = false;
		
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

		String bookNum = request.getParameter("bookNum");
		bookNum = "20250801-0018";
		// 결제 승인 API 호출되는지 확인하기 + 결제정보insert + 예약정보update 트랜잭션
		if (responseCode == 200) {
			System.out.println("결제 승인 성공");
			System.out.println("응답: " + result.toString());
			
			Connection dbConn = null;
			
			
			
			try {
				JSONParser parser = new JSONParser();
		        JSONObject jsonObj = (JSONObject) parser.parse(result.toString());

		        long amountLong = (Long) jsonObj.get("totalAmount");
		        String approvedAtStr = (String) jsonObj.get("approvedAt");
		        Timestamp approvedAt = Timestamp.from(Instant.parse(approvedAtStr));
		        String method = (String) jsonObj.get("method");
		        System.out.println(">> 받은 method: " + method);  
		        if (method == null) method = "UNKNOWN";
		        
		        //db커넥션
				dbConn = DBConnector.getConn();
				dbConn.setAutoCommit(false);
				
				//세션에서 유저고유
				HttpSession session = request.getSession();
			    Long usersNum = (Long) session.getAttribute("usersNum");
			    if (usersNum == null) {
			        throw new IllegalStateException("세션에서 사용자 번호(usersNum)를 가져올 수 없습니다.");
			    }
			    
				//payments.jsp에서 bookNum을 받아서 예약번호로 예약상태 업데이트
				BookDao.getInstance().updateBookStatus(dbConn,bookNum);
				PaymentDao payDao = PaymentDao.getInstance();
				PaymentDto payDto = new PaymentDto();
				payDto.setPayNum(payDao.generatePayNum());
				payDto.setpayBookNum(bookNum);
				payDto.setOrderId(orderId);
				payDto.setPayUserNum(usersNum);
				payDto.setPayAmount(amountLong);
				payDto.setPayMethodGroupId("PAYMENT_METHOD");
				
				// method 값을 영어 코드로 매핑
				String methodCode;
				switch (method) {
				    case "카드":
				        methodCode = "CARD";
				        break;
				    case "계좌이체":
				        methodCode = "TRANSFER";
				        break;
				    case "휴대폰":
				    case "휴대폰결제":
				        methodCode = "MOBILE_PHONE";
				        break;
				    default:
				        methodCode = "UNKNOWN";
				}

				switch (methodCode) {
				    case "CARD":
				        payDto.setPayMethodCode(10);
				        break;
				    case "TRANSFER":
				        payDto.setPayMethodCode(11);
				        break;
				    case "MOBILE_PHONE":
				        payDto.setPayMethodCode(12);
				        break;
				    default:
				        payDto.setPayMethodCode(0); // 잘못된 결제 수단
				}


				payDto.setPayStatusGroupId("PAYMENT_STATUS");
				payDto.setPayStatusCode(11);
				//날짜 설정
				payDto.setPayApprovedAt(approvedAt);
				payDto.setPayCreatedAt(approvedAt);
				payDto.setPayPaidAt(approvedAt);
				
				boolean payInsertSuccess = payDao.insertPayment(dbConn, payDto);
				if(!payInsertSuccess) {
					throw new Exception("결제 정보를 DB에 삽입 실패 했습니다.");
				}
				dbConn.commit();
				paymentSuccess = true;
				
				}catch (Exception e) {
		            e.printStackTrace();
		            if (dbConn != null) try { dbConn.rollback(); } catch (Exception rollbackEx) {}
		            System.out.println("결제가 실패하여, 예약이 취소 되었습니다.");
		            BookDao.getInstance().deleteByBookNum("20250801-0017"); // 예약 삭제 bookNum
		        } finally {
		            if (dbConn != null) try { dbConn.close(); } catch (Exception e) {}
		            
		        }
			
		
			//결제 승인 api 호출 실패
		} else {
			System.out.println("결제 승인 실패");
			System.out.println("응답 코드: " + responseCode);
			System.out.println("에러 메시지: " + result.toString());
		}
		
		// 결제 완료/실패 정보를 가지고 결과페이지로 포워딩
		if (paymentSuccess) {
			BookDto bookingDto = BookDao.getInstance().getByBookNum(bookNum);
			PaymentDto paymentDto = PaymentDao.getInstance().getPayByOrderId(orderId);
			
			request.setAttribute("paymentSuccess", true);
			request.setAttribute("booking", bookingDto);
			request.setAttribute("payment", paymentDto);
			
			RequestDispatcher rd = request.getRequestDispatcher("/pay/pay-result.jsp");
			rd.forward(request, response);
			
		} else {
		    response.sendRedirect(request.getContextPath() + "/pay/pay-result.jsp?success=0");
		}

	}

}
