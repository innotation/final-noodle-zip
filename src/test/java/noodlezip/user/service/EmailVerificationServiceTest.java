package noodlezip.user.service;

import noodlezip.common.mail.MailService;
import noodlezip.common.redis.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String APP_BASE_URL = "http://localhost:8080";
    @BeforeEach
    void setUp() {
        // 테스트 환경에서는 실제 Spring 컨텍스트가 로드되지 않으니 RefeliectionTestUtils를 통해 의존성 주입
        ReflectionTestUtils.setField(emailVerificationService, "appBaseUrl", APP_BASE_URL);
    }

    @Test
    @DisplayName("이메일 전송 내부 실제 호출 검증")
    void sendVerificationEmail() {

        // Given

        // When
        emailVerificationService.sendVerificationCode(TEST_EMAIL);

        // Then 이메일 내용 검증, redis 호출 검증
        verify(mailService).sendEMail(
                eq(TEST_EMAIL),
                eq("[NoodleZip] 이메일 주소 인증"),
                argThat(content -> {
                    assertThat(content).contains(APP_BASE_URL + "/verify-email?email=" + TEST_EMAIL);
                    assertThat(content).containsPattern("code=[A-Z0-9]{6}"); // 6자리 코드가 패턴으로 존재하는지 확인
                    assertThat(content).contains("이메일 인증하기"); // 버튼 텍스트
                    assertThat(content).contains("이 링크는 10분 동안 유효합니다."); // 만료 시간 텍스트
                    return true;
                }),
                eq(true)
        );

        verify(redisRepository).setWithExpire(
                eq(TEST_EMAIL),
                anyString(),
                eq(10L),
                eq(TimeUnit.MINUTES)
        );
        verifyNoMoreInteractions(mailService, redisRepository);
    }
}