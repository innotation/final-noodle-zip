package noodlezip.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendEMail(String to, String subject, String text) {
        SimpleMailMessage message = createEmailForm(to, subject, text);
        try {
            javaMailSender.send(message);
            log.debug("Mail sent");
            log.debug("Mail sent to {}", to);
            log.debug("Mail subject {}", subject);
            log.debug("Mail text {}", text);
        } catch (Exception e) {
            log.error("Failed to send email", e);
            log.debug("Mail sent to {}", to);
            log.debug("Mail subject {}", subject);
            log.debug("Mail text {}", text);
        }

    }

    private SimpleMailMessage createEmailForm(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        return message;
    }
}
