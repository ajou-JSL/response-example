package study.spring_security_jwt.email.service;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import study.spring_security_jwt.auth.service.SignupService;
import study.spring_security_jwt.global.error.ErrorCode;
import study.spring_security_jwt.global.error.exception.CustomException;
import study.spring_security_jwt.redis.util.RedisUtil;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String sender;

    private MimeMessage createMessage(String code, String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject(" 모음(MOUM) 에서 발송된 인증 번호입니다.");
        message.setText("이메일 인증코드: "+code);
        message.setFrom(sender);

        return  message;
    }

    public void sendMail(String code, String email) throws Exception{
        try{
            MimeMessage mimeMessage = createMessage(code, email);
            javaMailSender.send(mimeMessage);
        }catch (MailException mailException){
            mailException.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    public String sendCertificationMail(String email) throws Exception {
        String code = UUID.randomUUID().toString().substring(0, 6); //랜덤 인증번호 : uuid
        sendMail(code,email);

        redisUtil.setDataExpire(code,email,60*1L); // {key,value} 1분동안 저장.
        return  code;
    }
}