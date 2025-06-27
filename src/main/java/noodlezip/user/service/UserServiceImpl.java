package noodlezip.user.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.mail.MailService;
import noodlezip.common.redis.RedisRepository;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.ActiveStatus;
import noodlezip.user.entity.User;
import noodlezip.user.entity.UserType;
import noodlezip.user.repository.UserRepository;
import noodlezip.user.status.UserErrorStatus;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final RedisRepository redisRepository;
    private final MailService mailService;

    @Override
    @Transactional
    public void registUser(UserDto user) {

        userRepository.findByLoginId(user.getLoginId()).ifPresent((existingUser) -> {
                throw new CustomException(UserErrorStatus._ALREADY_EXIST_LOGIN_ID);
        });

        userRepository.findByEmail(user.getEmail()).ifPresent((existingUser) -> {
            throw new CustomException(UserErrorStatus._ALREADY_EXIST_EMAIL);
        });

        User newUser = modelMapper.map(user, User.class);
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setUserType(UserType.NORMAL);
        newUser.setActiveStatus(ActiveStatus.ACTIVE);
        newUser.setIsEmailVerified(false);

        String email = newUser.getEmail();
        // 이메일 전송
        mailService.sendEMail(email, "test", "test email");

        // 이메일, 코드 저장
        redisRepository.setWithExpire(email, "code", 10L, TimeUnit.MINUTES);

        userRepository.save(newUser);

    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLoginIdDuplicated(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailDuplicated(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
