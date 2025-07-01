package noodlezip.user.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import noodlezip.common.auth.MyUserDetails;
import noodlezip.user.entity.User;
import noodlezip.user.entity.UserType;
import noodlezip.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Mockito를 JUnit 5에서 사용하기 위한 설정
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser; // 테스트에 사용할 User 객체

    @BeforeEach // 각 테스트 메서드 실행 전에 호출됩니다.
    void setUp() {
        // 테스트용 사용자 객체 초기화 (실제 DB 데이터와 유사하게 설정)
        testUser = User.builder()
                .id(1L)
                .loginId("testUser01")
                .password("encodedPassword") // 실제는 BCrypt로 인코딩된 비밀번호여야 합니다.
                .userName("테스트 사용자")
                .userType(UserType.NORMAL) // UserType.NORMAL 또는 UserType.ADMIN
                .build();
    }

    @DisplayName("정상적인 사용자 정보 로드 테스트")
    @Test
    void testLoadUserByUsername_Success() {
        // Given: userRepository.findByLoginId("testUser01") 호출 시 testUser 객체를 반환하도록 설정
        when(userRepository.findByLoginId("testUser01")).thenReturn(Optional.of(testUser));

        // When: loadUserByUsername 메서드 호출
        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser01");

        // Then:
        // 1. userRepository.findByLoginId("testUser01")가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId("testUser01");

        // 2. 반환된 UserDetails 객체가 null이 아닌지 확인
        assertNotNull(userDetails);

        // 3. 반환된 UserDetails의 username이 예상과 일치하는지 확인
        assertEquals("testUser01", userDetails.getUsername());

        // 4. 반환된 UserDetails의 password가 예상과 일치하는지 확인
        assertEquals("encodedPassword", userDetails.getPassword());

        // 5. 반환된 UserDetails가 MyUserDetails 타입인지 확인 (선택 사항)
        assertTrue(userDetails instanceof MyUserDetails);

        // 6. UserDetails의 권한(Authority)이 올바르게 설정되었는지 확인 (MyUserDetails 구현에 따라 다름)
        // 예를 들어, MyUserDetails에 권한을 가져오는 메서드가 있다면:
         assertEquals(1, userDetails.getAuthorities().size());
         assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("NORMAL")));
    }

    @DisplayName("존재하지 않는 사용자 정보 로드 시 UsernameNotFoundException 발생 테스트")
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Given: userRepository.findByLoginId("nonExistentUser") 호출 시 null을 반환하도록 설정
        when(userRepository.findByLoginId("nonExistentUser")).thenReturn(Optional.empty());

        // When & Then: loadUserByUsername 호출 시 UsernameNotFoundException이 발생하는지 확인
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonExistentUser");
        });

        // userRepository.findByLoginId("nonExistentUser")가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).findByLoginId("nonExistentUser");
    }
}