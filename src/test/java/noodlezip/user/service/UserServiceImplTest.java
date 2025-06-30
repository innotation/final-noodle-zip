package noodlezip.user.service;

import noodlezip.common.exception.CustomException;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.ActiveStatus;
import noodlezip.user.entity.User;
import noodlezip.user.entity.UserType;
import noodlezip.user.repository.UserRepository;
import noodlezip.user.status.UserErrorStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto testUserDto;

    private User testUser;
    @BeforeEach
    void setUp() {
        testUserDto = UserDto.builder()
                .loginId("test")
                .userName("test")
                .password("q12345678@")
                .birth("20000311")
                .email("test@test.com")
                .phone("010-1234-5678")
                .gender("M")
                .build();

        testUser = User.builder()
                .loginId(testUserDto.getLoginId())
                .userName(testUserDto.getUserName())
                .password(testUserDto.getPassword())
                .birth(testUserDto.getBirth())
                .email(testUserDto.getEmail())
                .phone(testUserDto.getPhone())
                .gender(testUserDto.getGender())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void registUser() {
        // Given: userRepository.findByLoginId, findByEmail 중복 없음 가정
        when(userRepository.findByLoginId(testUser.getLoginId())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        User newUserEntity = testUser;
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        // When:

        userService.registUser(testUser);

        // Then:


        // 1. findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId(testUserDto.getLoginId());
        // 2. findByEmail이 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail(testUserDto.getEmail());
        // 3. UserRepository.save가 한 번 호출되었고, 인코딩된 비밀번호와 올바른 속성을 가진 User 객체로 호출되었는지 확인
        verify(userRepository, times(1)).save(argThat(user ->
                user.getLoginId().equals(testUserDto.getLoginId()) &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getEmail().equals(testUserDto.getEmail()) &&
                        user.getUserName().equals(testUserDto.getUserName()) &&
                        user.getUserType() == UserType.NORMAL &&
                        user.getActiveStatus() == ActiveStatus.ACTIVE &&
                        !user.getIsEmailVerified()
        ));
    }

    @DisplayName("중복된 로그인 ID로 사용자 등록 시 CustomException 발생 테스트")
    @Test
    void registUser_DuplicateLoginId() { // 사용자 요청에 따라 메서드 이름 변경
        // Given:
        // 1. findByLoginId 호출 시 이미 존재하는 User 객체를 Optional로 반환
        when(userRepository.findByLoginId(testUserDto.getLoginId())).thenReturn(Optional.of(new User()));

        // When & Then: registUser 호출 시 CustomException이 발생하는지 확인
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.registUser(testUser);
        });

        // 예외의 상태 코드가 _ALREADY_EXIST_LOGIN_ID와 일치하는지 확인
        assertEquals(UserErrorStatus._ALREADY_EXIST_LOGIN_ID, exception.getErrorCode());

        // findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId(testUserDto.getLoginId());
        // findByEmail은 호출되지 않아야 함 (첫 번째 중복 검사에서 예외 발생)
        verify(userRepository, never()).findByEmail(anyString());
        // save 메서드는 호출되지 않아야 함
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("중복된 이메일로 사용자 등록 시 CustomException 발생 테스트")
    @Test
    void registUser_DuplicateEmail() { // 사용자 요청에 따라 메서드 이름 변경
        // Given:
        // 1. findByLoginId 호출 시 중복 없음
        when(userRepository.findByLoginId(testUserDto.getLoginId())).thenReturn(Optional.empty());
        // 2. findByEmail 호출 시 이미 존재하는 User 객체를 Optional로 반환
        when(userRepository.findByEmail(testUserDto.getEmail())).thenReturn(Optional.of(new User()));

        // When & Then: registUser 호출 시 CustomException이 발생하는지 확인
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.registUser(testUser);
        });

        // 예외의 상태 코드가 _ALREADY_EXIST_EMAIL와 일치하는지 확인
        assertEquals(UserErrorStatus._ALREADY_EXIST_EMAIL, exception.getErrorCode());

        // findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId(testUserDto.getLoginId());
        // findByEmail이 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail(testUserDto.getEmail());
        // save 메서드는 호출되지 않아야 함
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("로그인 ID 중복 여부 확인 테스트 - 중복 없음")
    @Test
    void testIsLoginIdDuplicated_NotDuplicated() {
        // Given: findByLoginId 호출 시 Optional.empty() 반환
        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.empty());

        // When: isLoginIdDuplicated 메서드 호출
        boolean isDuplicated = userService.isLoginIdDuplicated("uniqueLoginId");

        // Then: false가 반환되는지 확인
        assertFalse(isDuplicated);
        // findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId("uniqueLoginId");
    }

    @DisplayName("로그인 ID 중복 여부 확인 테스트 - 중복 있음")
    @Test
    void testIsLoginIdDuplicated_Duplicated() {
        // Given: findByLoginId 호출 시 Optional.of(User) 반환
        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.of(new User()));

        // When: isLoginIdDuplicated 메서드 호출
        boolean isDuplicated = userService.isLoginIdDuplicated("existingLoginId");

        // Then: true가 반환되는지 확인
        assertTrue(isDuplicated);
        // findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId("existingLoginId");
    }

    @DisplayName("이메일 중복 여부 확인 테스트 - 중복 없음")
    @Test
    void testIsEmailDuplicated_NotDuplicated() {
        // Given: findByEmail 호출 시 Optional.empty() 반환
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When: isEmailDuplicated 메서드 호출
        boolean isDuplicated = userService.isEmailDuplicated("unique@example.com");

        // Then: false가 반환되는지 확인
        assertFalse(isDuplicated);
        // findByEmail이 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail("unique@example.com");
    }

    @DisplayName("이메일 중복 여부 확인 테스트 - 중복 있음")
    @Test
    void testIsEmailDuplicated_Duplicated() {
        // Given: findByEmail 호출 시 Optional.of(User) 반환
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // When: isEmailDuplicated 메서드 호출
        boolean isDuplicated = userService.isEmailDuplicated("existing@example.com");

        // Then: true가 반환되는지 확인
        assertTrue(isDuplicated);
        // findByEmail이 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail("existing@example.com");
    }
}