package noodlezip.user.service;

import lombok.RequiredArgsConstructor;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Map<String, String> registUser(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        String msg = null;
        String path = null;


        return Map.of();
    }
}
