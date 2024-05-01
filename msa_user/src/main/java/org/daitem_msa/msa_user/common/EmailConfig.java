package org.daitem_msa.msa_user.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class EmailConfig {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.debug}")
    private boolean debug;

    @Value("${spring.mail.properties.mail.smtp.starttls.ssl.trust}")
    private String sslTrust;

    @Value("${spring.mail.properties.mail.smtp.starttls.ssl.enable}")
    private boolean sslEnable;


    // 이메일 제목
    String subject = "[daitem_offical] 인증을 완료해 주세요.";

    // 인증번호
    String verificationCode = generateVerificationCode();

    // 실제로 발송되는 메일 내용 html
    String body = makeEmailMessage(verificationCode);


    public void sendMail(String to){

        // SMTP 서버 설정
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.host",host);
        props.put("mail.port",port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.starttls.debug", debug);
        props.put("mail.smtp.starttls.ssl.trust", sslTrust);
        props.put("mail.smtp.starttls.ssl.enable", sslEnable);

        // SMTP Session 생성
        Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator(){
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });

        try {
            // 메시지 생성
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body, "utf-8", "html");

            System.out.println("이메일 전송에 시작.");
            Transport.send(message);
            System.out.println("이메일 전송에 성공했습니다.");

            // 레디스 서버에 인증 코드 저장
            saveVerificationCodeToRedis(verificationCode, to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        // 랜덤한 문자와 숫자를 포함한 문자열을 담을 StringBuilder 생성
        StringBuilder sb = new StringBuilder();
        int length = 7;

        // 랜덤한 문자와 숫자를 생성하기 위한 문자열
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Random 객체 생성
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }

    private String makeEmailMessage(String verificationCode) {

        String certificationMessage = "<html><body>";
        certificationMessage += "<h1>안녕하세요, 다잇템입니다.</h1>";
        certificationMessage += "<p>회원가입 인증 번호는 <strong>" + verificationCode + "</strong>입니다.</p>";
        certificationMessage += "<p>아래 확인 버튼을 눌러 인증을 완료해 주세요. 감사합니다.</p>";
        certificationMessage += "<form action=\"http://localhost:8080/api/v1/user/check-verify-code\" method=\"POST\">";
        certificationMessage += "<input type=\"text\" name=\"userInput\" placeholder=\"인증 코드 입력\">";
        certificationMessage += "<input type=\"submit\" value=\"확인\">";
        certificationMessage += "</form>";
        certificationMessage += "</body></html>";
        return certificationMessage;
    }

    //레디스에 인증코드, 이메일 저장
    private void saveVerificationCodeToRedis(String verificationCode, String to) {
        System.out.println("레디스 메소드 시작");
        try{
            redisTemplate.opsForValue().set("verificationCode", verificationCode);
            System.out.println("저장 ? " + verificationCode);
            redisTemplate.opsForValue().set("userEmail", to);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("레디스 인증실패");
        }
    }

    public boolean checkVerificationCode(String userInput) {
        String verificationCode = redisTemplate.opsForValue().get("verificationCode");
        return userInput.equals(verificationCode);
    }

}