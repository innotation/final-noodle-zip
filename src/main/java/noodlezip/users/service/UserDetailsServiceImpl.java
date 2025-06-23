package noodlezip.users.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.auth.LoginUser;
import noodlezip.users.entity.User;
import noodlezip.users.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username);

        if (user == null) {
            throw new UsernameNotFoundException("Name is Not Found: " + username);
        }

        List<GrantedAuthority> authorityList = Collections.singletonList(
                new SimpleGrantedAuthority(user.getUserType().name()) // enum의 이름 (ADMIN, NORMAL) 사용
        );
        log.info("Loading user {} from database", username);
        return new LoginUser(user.getLoginId(), user.getPassword(), authorityList, user.getUserName());
    }
}
