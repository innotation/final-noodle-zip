package noodlezip.user.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import noodlezip.common.mail.MailService;
import noodlezip.common.redis.RedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationService {

    private final RedisRepository redisRepository;
    private final MailService mailService;

    private String appBaseUrl="http://localhost:8080";

    public void sendVerificationCode(String email) {
        // 인증 코드 생성
        String verificationCode = generateVerificationCode();

        // 인증 링크 생성
        String verificationLink = appBaseUrl + "/verify-email?email=" + email + "&code=" + verificationCode;
        String emailContent = createEmailContent(verificationLink);

        // 이메일 전송
        mailService.sendEMail(email, "[NoodleZip] 이메일 주소 인증", emailContent, true);

        // 이메일, 코드 저장
        redisRepository.setWithExpire(email, verificationCode, 10L, TimeUnit.MINUTES);
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private String createEmailContent(String verificationLink) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2>이메일 주소를 인증해주세요</h2>" +
                "<p>NoodleZip 가입을 환영합니다!</p>" +
                "<p>이메일 인증을 완료하려면 아래 링크를 클릭해주세요:</p>" +
                "<p><a href=\"" + verificationLink + "\" " +
                "style='display: inline-block; padding: 10px 20px; background-color: #589442; color: white; text-decoration: none; border-radius: 5px;'>" +
                "이메일 인증하기</a></p>" +
                "<p>이 링크는 10분 동안 유효합니다.</p>" +
                "<p>만약 본인이 요청한 것이 아니라면, 이 이메일을 무시해주세요.</p>" +
                "<p>감사합니다.</p>" +
                "</body>" +
                "</html>";
    }
}
