package noodlezip.common.auth;

import lombok.RequiredArgsConstructor;
import noodlezip.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getUserType().name()));
        // enum의 이름 (ADMIN, NORMAL) 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 정지 관련 설정, 이후에 정지 기능 구현 이후 수정 예정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 이메일 인증시 활성화
        return user.getIsEmailVerified();
    }

    // 암호화 된 패스워드
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 로그인 ID
    @Override
    public String getUsername() {
        return user.getLoginId();
    }
}
