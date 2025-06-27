package noodlezip.user.repository;

import noodlezip.user.entity.ActiveStatus;
import noodlezip.user.entity.User;
import noodlezip.user.entity.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("로그인 ID로 사용자 찾기 - 성공")
    @Test
    void findByLoginId_Success() {
        // Given: 테스트용 사용자 데이터 생성 및 영속화
        User user = User.builder()
                .loginId("testLoginId")
                .password("encodedPassword")
                .email("test@example.com")
                .userName("테스트유저")
                .userType(UserType.NORMAL)
                .activeStatus(ActiveStatus.ACTIVE)
                .birth("20000311")
                .phone("010-9141-8063")
                .gender("M")
                .isEmailVerified(false)
                .build();
        entityManager.persist(user);
        entityManager.flush();

        // When: findByLoginId 메서드 호출
        Optional<User> foundUser = userRepository.findByLoginId("testLoginId");

        // Then:
        // 1. Optional이 비어있지 않은지 확인
        assertThat(foundUser).isPresent();
        // 2. 찾아온 사용자의 로그인 ID가 예상과 일치하는지 확인
        assertThat(foundUser.get().getLoginId()).isEqualTo("testLoginId");
    }

    @DisplayName("로그인 ID로 사용자 찾기 - 실패 (존재하지 않는 ID)")
    @Test
    void findByLoginId_NotFound() {
        // Given: 아무 사용자도 저장하지 않음

        // When: 존재하지 않는 로그인 ID로 findByLoginId 메서드 호출
        Optional<User> foundUser = userRepository.findByLoginId("nonExistentId");

        // Then: Optional이 비어있는지 확인
        assertThat(foundUser).isNotPresent();
    }

    @DisplayName("이메일로 사용자 찾기 - 성공")
    @Test
    void findByEmail_Success() {
        // Given: 테스트용 사용자 데이터 생성 및 영속화
        User user = User.builder()
                .loginId("anotherLoginId")
                .password("anotherEncodedPassword")
                .email("another@example.com")
                .userName("다른테스트유저")
                .gender("M")
                .birth("20000311")
                .phone("010-9141-8063")
                .userType(UserType.NORMAL)
                .activeStatus(ActiveStatus.ACTIVE)
                .isEmailVerified(false)
                .build();
        entityManager.persist(user); // DB에 저장
        entityManager.flush(); // 변경사항 즉시 반영

        // When: findByEmail 메서드 호출
        Optional<User> foundUser = userRepository.findByEmail("another@example.com");

        // Then:
        // 1. Optional이 비어있지 않은지 확인
        assertThat(foundUser).isPresent();
        // 2. 찾아온 사용자의 이메일이 예상과 일치하는지 확인
        assertThat(foundUser.get().getEmail()).isEqualTo("another@example.com");
    }

    @DisplayName("이메일로 사용자 찾기 - 실패 (존재하지 않는 이메일)")
    @Test
    void findByEmail_NotFound() {
        // Given: 아무 사용자도 저장하지 않음

        // When: 존재하지 않는 이메일로 findByEmail 메서드 호출
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then: Optional이 비어있는지 확인
        assertThat(foundUser).isNotPresent();
    }
}