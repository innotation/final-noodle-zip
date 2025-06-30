package noodlezip.common.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendEMail(String to, String subject, String text, boolean isHtml) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true: 멀티파트 메시지 활성화, "UTF-8": 인코딩
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml); // 여기에 isHtml 파라미터를 넘겨줍니다.

            javaMailSender.send(message);
            log.info("이메일 전송 성공: TO = {}, Subject = {}", to, subject);
        } catch (Exception e) {
            log.error("이메일 전송 실패: TO = {}, Subject = {}", to, subject, e);
            throw new RuntimeException("이메일 전송 실패", e); // 예외 처리 필요
        }
    }
}
