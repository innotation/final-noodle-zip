package noodlezip.user.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.FileUtil;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.ActiveStatus;
import noodlezip.user.entity.User;
import noodlezip.user.entity.UserType;
import noodlezip.user.repository.UserRepository;
import noodlezip.user.status.UserErrorStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final FileUtil fileUtil;

    @Override
    @Transactional
    public void registUser(User user) {

        userRepository.findByLoginId(user.getLoginId()).ifPresent((existingUser) -> {
            throw new CustomException(UserErrorStatus._ALREADY_EXIST_LOGIN_ID);
        });

        userRepository.findByEmail(user.getEmail()).ifPresent((existingUser) -> {
            throw new CustomException(UserErrorStatus._ALREADY_EXIST_EMAIL);
        });

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.NORMAL);
        user.setActiveStatus(ActiveStatus.ACTIVE);
        user.setIsEmailVerified(false);

        String email = user.getEmail();

        // 이메일 서비스 호출(전송 및 인증코드 생성을 통해 redis 저장)
        emailVerificationService.sendVerificationCode(email);
        userRepository.save(user);
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

    @Override
    @Transactional // DB 업데이트와 Redis 삭제를 하나의 트랜잭션으로 묶음
    public void verifyUserEmail(String email, String code) {
        // 코드 검증
        if (emailVerificationService.verifyEmail(email, code)) {
            // 검증 성공 시, Redis에서 해당 코드 삭제(emailVerifService의 책임)
            emailVerificationService.deleteCode(email);
            // 유저 활성화
            userRepository.findByEmail(email).ifPresentOrElse(
                    (existingUser) -> {
                        existingUser.setIsEmailVerified(true);
                        userRepository.save(existingUser);
                    },
                    () -> {
                        // 유저 미존재 case
                        throw new CustomException(UserErrorStatus._NOT_FOUND_USER);
                    }
            );
        }
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserDto userDto, MultipartFile profileImage, MultipartFile bannerImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorStatus._NOT_FOUND_USER));
        Map<String, String> profileMap = fileUtil.fileupload("profile", profileImage);
        Map<String, String> bannerMap = fileUtil.fileupload("banner", bannerImage);
        user.setEmail(user.getEmail());
        user.setProfileImageUrl(profileMap.get("fileUrl"));
        user.setProfileBannerImageUrl(bannerMap.get("fileUrl"));
        userRepository.save(user);
    }
}
