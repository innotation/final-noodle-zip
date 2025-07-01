package noodlezip.user.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.mail.MailService;
import noodlezip.common.redis.RedisRepository;
import noodlezip.user.status.UserErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional

public class EmailVerificationService {

    private final RedisRepository redisRepository;
    private final MailService mailService;

    @Value("${app.base.url}")
    private String appBaseUrl;

    public boolean verifyEmail(String email, String code) {

        // redis에서 이메일을 통해 코드 불러오기
        Optional<String> storedCode = redisRepository.get(email);

        if (storedCode.isPresent()) { // 값이 존재할 경우
            if (storedCode.get().equals(code)) { // 코드 일치시
                return true;
            }
            else { // " 불일치 시
                throw new CustomException(UserErrorStatus._MISS_MATCH_AUTH_CODE);
            }
        } else { // 값 미존재 시
            throw new CustomException(UserErrorStatus._EXPIRED_AUTH_CODE);
        }
    }

    public boolean deleteCode(String email) {
        return redisRepository.delete(email);
    }


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
