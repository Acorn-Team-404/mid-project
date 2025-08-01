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
        String fromEmail = "izeroi313@gmail.com";
        String appPassword = "bpxz fcck upae vcah";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("회원가입 이메일 인증 코드");
            message.setText("인증 코드: " + authCode);

            Transport.send(message);
            System.out.println("이메일 전송 성공");
            System.out.println("발송된 인증코드: " + authCode); // ✅ 여기 추가

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

