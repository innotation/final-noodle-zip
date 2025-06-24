package noodlezip.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.auth.MyUserDetails;
import noodlezip.users.entity.User;
import noodlezip.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        log.info("Loading user {} from database", username);
        return new MyUserDetails(user);
    }
}
