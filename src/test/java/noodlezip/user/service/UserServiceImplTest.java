package noodlezip.user.service;

import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.FileUtil;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.ActiveStatus;
import noodlezip.user.entity.User;
import noodlezip.user.entity.UserType;
import noodlezip.user.repository.UserRepository;
import noodlezip.user.status.UserErrorStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceImplTest {

    @Mock
    private FileUtil fileUtil;

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
    private MultipartFile mockProfileImage;
    private MultipartFile mockBannerImage;

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
                .isEmailVerified(false)
                .profileImageUrl("old_profile.jpg")
                .profileBannerImageUrl("old_banner.jpg")
                .build();

        mockProfileImage = new MockMultipartFile(
                "profileImage", "new_profile.jpg", "image/jpeg", "profile-data".getBytes());
        mockBannerImage = new MockMultipartFile(
                "bannerImage", "new_banner.jpg", "image/jpeg", "banner-data".getBytes());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void registUser_success() {
        // Given
        // userRepository.findByLoginId, findByEmail 중복 없음 가정
        when(userRepository.findByLoginId(testUser.getLoginId())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword"); // 비밀번호 인코딩 Mock

        // when(userRepository.save(any(User.class))).thenReturn(testUser); // save 호출 확인을 위해 필요하다면 추가

        // When
        userService.registUser(testUser); // UserDto를 인자로 받는 registUser 메서드라고 가정

        // Then
        // 1. findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId(testUserDto.getLoginId());
        // 2. findByEmail이 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail(testUserDto.getEmail());
        // 3. UserRepository.save가 한 번 호출되었고, 인코딩된 비밀번호와 올바른 속성을 가진 User 객체로 호출되었는지 확인
        // ArgumentCaptor를 사용하여 save에 전달된 User 객체의 속성을 더 명확하게 검증
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getLoginId()).isEqualTo(testUserDto.getLoginId());
        assertThat(capturedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(capturedUser.getEmail()).isEqualTo(testUserDto.getEmail());
        assertThat(capturedUser.getUserName()).isEqualTo(testUserDto.getUserName());
        assertThat(capturedUser.getUserType()).isEqualTo(UserType.NORMAL);
        assertThat(capturedUser.getActiveStatus()).isEqualTo(ActiveStatus.ACTIVE);
        assertThat(capturedUser.getIsEmailVerified()).isFalse();

        // bCryptPasswordEncoder.encode가 한 번 호출되었는지 확인
        verify(bCryptPasswordEncoder, times(1)).encode(testUserDto.getPassword());
    }

    @DisplayName("중복된 로그인 ID로 사용자 등록 시 CustomException 발생 테스트")
    @Test
    void registUser_DuplicateLoginId() {
        // Given
        // findByLoginId 호출 시 이미 존재하는 User 객체를 Optional로 반환
        when(userRepository.findByLoginId(testUserDto.getLoginId())).thenReturn(Optional.of(new User()));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.registUser(testUser);
        });

        // 예외의 상태 코드가 _ALREADY_EXIST_LOGIN_ID와 일치하는지 확인
        assertThat(exception.getErrorCode()).isEqualTo(UserErrorStatus._ALREADY_EXIST_LOGIN_ID);

        // findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId(testUserDto.getLoginId());
        // findByEmail은 호출되지 않아야 함 (첫 번째 중복 검사에서 예외 발생)
        verify(userRepository, never()).findByEmail(anyString());
        // save 메서드는 호출되지 않아야 함
        verify(userRepository, never()).save(any(User.class));
        // 비밀번호 인코딩도 호출되지 않아야 함
        verify(bCryptPasswordEncoder, never()).encode(anyString());
    }

    @DisplayName("중복된 이메일로 사용자 등록 시 CustomException 발생 테스트")
    @Test
    void registUser_DuplicateEmail() {
        // Given
        // findByLoginId 호출 시 중복 없음
        when(userRepository.findByLoginId(testUserDto.getLoginId())).thenReturn(Optional.empty());
        // findByEmail 호출 시 이미 존재하는 User 객체를 Optional로 반환
        when(userRepository.findByEmail(testUserDto.getEmail())).thenReturn(Optional.of(new User()));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.registUser(testUser);
        });

        // 예외의 상태 코드가 _ALREADY_EXIST_EMAIL와 일치하는지 확인
        assertThat(exception.getErrorCode()).isEqualTo(UserErrorStatus._ALREADY_EXIST_EMAIL);

        // findByLoginId가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId(testUserDto.getLoginId());
        // findByEmail이 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail(testUserDto.getEmail());
        // save 메서드는 호출되지 않아야 함
        verify(userRepository, never()).save(any(User.class));
        // 비밀번호 인코딩도 호출되지 않아야 함
        verify(bCryptPasswordEncoder, never()).encode(anyString());
    }

    @DisplayName("로그인 ID 중복 여부 확인 테스트 - 중복 없음")
    @Test
    void testIsLoginIdDuplicated_NotDuplicated() {
        String uniqueLoginId = "uniqueLoginId";
        // Given
        when(userRepository.findByLoginId(uniqueLoginId)).thenReturn(Optional.empty());

        // When
        boolean isDuplicated = userService.isLoginIdDuplicated(uniqueLoginId);

        // Then
        assertThat(isDuplicated).isFalse();
        verify(userRepository, times(1)).findByLoginId(uniqueLoginId);
    }

    @DisplayName("로그인 ID 중복 여부 확인 테스트 - 중복 있음")
    @Test
    void testIsLoginIdDuplicated_Duplicated() {
        String existingLoginId = "existingLoginId";
        // Given
        when(userRepository.findByLoginId(existingLoginId)).thenReturn(Optional.of(new User()));

        // When
        boolean isDuplicated = userService.isLoginIdDuplicated(existingLoginId);

        // Then
        assertThat(isDuplicated).isTrue();
        verify(userRepository, times(1)).findByLoginId(existingLoginId);
    }

    @DisplayName("이메일 중복 여부 확인 테스트 - 중복 없음")
    @Test
    void testIsEmailDuplicated_NotDuplicated() {
        String uniqueEmail = "unique@example.com";
        // Given
        when(userRepository.findByEmail(uniqueEmail)).thenReturn(Optional.empty());

        // When
        boolean isDuplicated = userService.isEmailDuplicated(uniqueEmail);

        // Then
        assertThat(isDuplicated).isFalse();
        verify(userRepository, times(1)).findByEmail(uniqueEmail);
    }

    @DisplayName("이메일 중복 여부 확인 테스트 - 중복 있음")
    @Test
    void testIsEmailDuplicated_Duplicated1() {
        String existingEmail = "existing@example.com";
        // Given
        when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(new User()));

        // When
        boolean isDuplicated = userService.isEmailDuplicated(existingEmail);

        // Then
        assertThat(isDuplicated).isTrue();
        verify(userRepository, times(1)).findByEmail(existingEmail);
    }


    @DisplayName("이메일 중복 여부 확인 테스트 - 중복 있음")
    @Test
    void testIsEmailDuplicated_Duplicated2() {
        // Given: findByEmail 호출 시 Optional.of(User) 반환
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // When: isEmailDuplicated 메서드 호출
        boolean isDuplicated = userService.isEmailDuplicated("existing@example.com");

        // Then: true가 반환되는지 확인
        assertTrue(isDuplicated);
        // findByEmail이 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail("existing@example.com");
    }

    @Test
    @DisplayName("이메일 검증 성공 시: 이메일 인증 상태가 true로 변경되고 Redis 코드가 삭제된다")
    void verifyUserEmail_success() {
        String email = "test@example.com";
        String code = "123456";

        // Given
        // 1. emailVerificationService.verifyEmail이 true를 반환하도록 설정
        when(emailVerificationService.verifyEmail(email, code)).thenReturn(true);
        // 2. userRepository.findByEmail이 testUser를 Optional로 반환하도록 설정
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        // 3. userRepository.save가 어떤 User 객체를 받든 testUser를 반환하도록 설정 (save 호출 검증을 용이하게 함)
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        // userService의 verifyUserEmail 메서드를 호출
        userService.verifyUserEmail(email, code);

        // Then
        // 1. emailVerificationService.verifyEmail 메서드가 정확히 한 번 호출되었는지 확인
        verify(emailVerificationService, times(1)).verifyEmail(email, code);

        // 2. emailVerificationService.deleteCode 메서드가 정확히 한 번 호출되었는지 확인
        verify(emailVerificationService, times(1)).deleteCode(email);

        // 3. userRepository.findByEmail 메서드가 정확히 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByEmail(email);

        // 4. userRepository.save 메서드가 정확히 한 번 호출되었는지 확인
        verify(userRepository, times(1)).save(testUser); // testUser 객체가 save 인자로 전달되었는지 확인

        // 5. testUser 객체의 isEmailVerified 상태가 true로 변경되었는지 확인 (객체 상태 검증)
        assertThat(testUser.getIsEmailVerified()).isTrue();
    }

    @Test
    @DisplayName("이메일 검증 실패 시: 사용자 상태가 변경되지 않고 Redis 코드 삭제도 안 된다")
    void verifyUserEmail_verificationFailed() {
        String email = "test@example.com";
        String code = "wrongcode";

        // Given
        // emailVerificationService.verifyEmail이 false를 반환하도록 설정
        when(emailVerificationService.verifyEmail(email, code)).thenReturn(false);
        // userRepository.findByEmail은 호출되지 않을 것이므로 설정하지 않거나, 호출되더라도 Optional.empty()를 반환하도록 설정 (불필요)
        // userRepository.save는 호출되지 않을 것이므로 설정하지 않음

        // When
        userService.verifyUserEmail(email, code);

        // Then
        // 1. emailVerificationService.verifyEmail 메서드가 한 번 호출되었는지 확인
        verify(emailVerificationService, times(1)).verifyEmail(email, code);

        // 2. emailVerificationService.deleteCode 메서드가 호출되지 않았는지 확인
        verify(emailVerificationService, never()).deleteCode(email);

        // 3. userRepository.findByEmail 메서드가 호출되지 않았는지 확인
        verify(userRepository, never()).findByEmail(anyString());

        // 4. userRepository.save 메서드가 호출되지 않았는지 확인
        verify(userRepository, never()).save(any(User.class));

        // 5. testUser의 이메일 인증 상태가 변경되지 않고 초기값(false)을 유지하는지 확인
        assertThat(testUser.getIsEmailVerified()).isFalse();
    }

    @Test
    @DisplayName("이메일 검증 시 사용자가 존재하지 않으면 CustomException(_NOT_FOUND_USER) 발생")
    void verifyUserEmail_userNotFound() {
        String email = "nonexistent@example.com";
        String code = "123456";

        // Given (when)
        when(emailVerificationService.verifyEmail(email, code)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then (assertThrows)
        CustomException exception = assertThrows(CustomException.class, () ->
                userService.verifyUserEmail(email, code)
        );
        assertThat(exception.getErrorCode()).isEqualTo(UserErrorStatus._NOT_FOUND_USER);
    }

    @Test
    @DisplayName("사용자 정보와 이미지 모두 성공적으로 업데이트된다")
    void updateUser_successAll() {
        // Given (when)
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        Map<String, String> profileFileMap = new HashMap<>();
        profileFileMap.put("fileUrl", "http://new-profile.com/new_profile.jpg");
        when(fileUtil.fileupload("profile", mockProfileImage)).thenReturn(profileFileMap);

        Map<String, String> bannerFileMap = new HashMap<>();
        bannerFileMap.put("fileUrl", "http://new-banner.com/new_banner.jpg");
        when(fileUtil.fileupload("banner", mockBannerImage)).thenReturn(bannerFileMap);

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.updateUser(testUser.getId(), testUserDto, mockProfileImage, mockBannerImage);

        // Then (assertThat)
        assertThat(userRepository.findById(testUser.getId())).contains(testUser); // findById 호출 및 반환 확인
        assertThat(fileUtil.fileupload("profile", mockProfileImage)).isEqualTo(profileFileMap); // fileupload 호출 및 반환 확인
        assertThat(fileUtil.fileupload("banner", mockBannerImage)).isEqualTo(bannerFileMap); // fileupload 호출 및 반환 확인
        assertThat(testUser.getUserName()).isEqualTo(testUserDto.getUserName()); // userName 업데이트 확인
        assertThat(testUser.getProfileImageUrl()).isEqualTo("http://new-profile.com/new_profile.jpg"); // profileImageUrl 업데이트 확인
        assertThat(testUser.getProfileBannerImageUrl()).isEqualTo("http://new-banner.com/new_banner.jpg"); // bannerImageUrl 업데이트 확인
        assertThat(userRepository.save(testUser)).isEqualTo(testUser); // save 호출 확인
    }

    @Test
    @DisplayName("사용자가 존재하지 않을 때 updateUser 호출 시 CustomException(_NOT_FOUND_USER) 발생")
    void updateUser_userNotFound() {
        // Given (when)
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then (assertThrows)
        CustomException exception = assertThrows(CustomException.class, () ->
                userService.updateUser(99L, testUserDto, null, null)
        );
        assertThat(exception.getErrorCode()).isEqualTo(UserErrorStatus._NOT_FOUND_USER);
    }

    @Test
    @DisplayName("이미지 파일 없이 사용자 이름만 성공적으로 업데이트된다")
    void updateUser_userNameOnly_success() {
        // Given
        // 1. userRepository.findById가 testUser를 반환하도록 설정
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        // 2. userRepository.save가 어떤 User 객체를 받든 testUser를 반환하도록 설정
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        // profileImage와 bannerImage를 모두 null로 전달하여 호출
        userService.updateUser(testUser.getId(), testUserDto, null, null);

        // Then
        // 1. userRepository.findById 메서드가 정확히 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findById(testUser.getId());

        // 2. testUser 객체의 userName이 testUserDto의 userName으로 업데이트되었는지 확인
        assertThat(testUser.getUserName()).isEqualTo(testUserDto.getUserName());

        // 4. 프로필 이미지 URL은 변경되지 않고 기존 값(old_profile.jpg)을 유지하는지 확인
        assertThat(testUser.getProfileImageUrl()).isEqualTo("old_profile.jpg");

        // 5. 배너 이미지 URL은 변경되지 않고 기존 값(old_banner.jpg)을 유지하는지 확인
        assertThat(testUser.getProfileBannerImageUrl()).isEqualTo("old_banner.jpg");

        // 6. userRepository.save 메서드가 정확히 한 번 호출되었는지 확인
        // save에 전달된 testUser 객체의 최종 상태를 확인
        verify(userRepository, times(1)).save(testUser);
    }
}