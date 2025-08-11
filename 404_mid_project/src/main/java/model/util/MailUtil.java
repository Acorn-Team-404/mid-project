package model.util;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtil {
    public static void sendEmail(String toEmail, String authCode) {
    	long start = System.currentTimeMillis(); // 시작 시간
        String fromEmail = "izeroi313@gmail.com";
        String appPassword = "bpxz fcck upae vcah";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        // 타임아웃 & 종료 최적화
        props.put("mail.smtp.connectiontimeout", "3000"); // connect 3s
        props.put("mail.smtp.timeout", "5000");           // read 5s
        props.put("mail.smtp.writetimeout", "5000");      // write 5s
        props.put("mail.smtp.quitwait", "false");         // 종료 대기 단축

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("STAYLOG 회원가입 이메일 인증 코드");
     
            String htmlContent = String.format("""
            	    <html>
					  <body>
					    <div>
					      <div style="text-align: center; font-size: 24px; font-weight: bold; margin-bottom: 24px; color: #000000;">
					        이메일 인증 코드
					      </div>
					      <p style="text-align: center;">안녕하세요 😊 STAYLOG 입니다.</p>
					      <p style="text-align: center;">회원가입을 위한 인증 번호는 다음과 같습니다</p>
					      <div style="font-size: 32px; font-weight: bold; letter-spacing: 6px; text-align: center; margin: 30px 0; padding: 20px; background-color: #ffffff; border: 2px dashed #c1c1c1; border-radius: 15px;">
					        %s
					      </div>
					      <div style="text-align: center; font-size: 15px; font-weight: lighter; margin-bottom: 24px; color: #000000;">
					        본 메일은 자동 발송된 메일입니다.
					      </div>
					    </div>
					  </body>
					</html>
            	    """, authCode);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("이메일 전송 성공");
            System.out.println("발송된 인증코드: " + authCode); 
            long end = System.currentTimeMillis(); // 끝 시간
            System.out.println("이메일 전송 완료 (소요 시간: " + (end - start) + "ms)");

        } catch (MessagingException e) {
        	long end = System.currentTimeMillis();
            System.err.println("이메일 전송 실패 (소요 시간: " + (end - start) + "ms)");
            e.printStackTrace();
        }
    }
}

