package noodlezip.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.FileUtil;
import noodlezip.mypage.status.MyPageErrorStatus;
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

@Service
@RequiredArgsConstructor
@Slf4j
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
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new CustomException(UserErrorStatus._NOT_FOUND_USER);
                });
        user.setUserName(userDto.getUserName());

        // 프로필 이미지 변경
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String profileImageUrl = fileUtil.fileupload("profile", profileImage).get("fileUrl");
                if (profileImageUrl != null) {
                    fileUtil.deleteFileFromS3(user.getProfileImageUrl());
                    user.setProfileImageUrl(profileImageUrl);
                    log.info("User {} profile image updated to: {}", userId, profileImageUrl);
                }
            } catch (Exception e) {
                log.error("Failed to upload profile image for user {}: {}", userId, e.getMessage(), e);
                throw new CustomException(ErrorStatus._FILE_UPLOAD_FAILED);
            }
        }

        // 배너 이미지 변경
        if (bannerImage != null && !bannerImage.isEmpty()) {
            try {
                String bannerImageUrl = fileUtil.fileupload("banner", bannerImage).get("fileUrl");
                if (bannerImageUrl != null) {
                    fileUtil.deleteFileFromS3(user.getProfileBannerImageUrl());
                    user.setProfileBannerImageUrl(bannerImageUrl);
                    log.info("User {} banner image updated to: {}", userId, bannerImageUrl);
                }
            } catch (Exception e) {
                log.error("Failed to upload banner image for user {}: {}", userId, e.getMessage(), e);
                throw new CustomException(ErrorStatus._FILE_UPLOAD_FAILED);
            }
        }

        userRepository.save(user);

        log.info("User {} profile update completed.", userId);
    }

    @Override
    @Transactional
    public void signoutUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for deletion with ID: {}", userId);
                    return new CustomException(UserErrorStatus._NOT_FOUND_USER);
                });
        user.setActiveStatus(ActiveStatus.SIGNOUT);
        user.setUserName("탈퇴한사용자");

        // 없애줄 데이터 전부 여기서 soft-delete

        // 유저 이미지 파일들 삭제
        fileUtil.deleteFileFromS3(user.getProfileImageUrl());
        user.setProfileBannerImageUrl(null);
        fileUtil.deleteFileFromS3(user.getProfileBannerImageUrl());
        user.setProfileImageUrl(null);
        user.setIsEmailVerified(false);

        userRepository.save(user);
        log.info("User with ID: {} has been soft-deleted and data cleared.", userId);
    }
    @Transactional(readOnly = true)
    public void validateMyPageExistingUserByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MyPageErrorStatus._NOT_FOUND_USER_MY_PAGE));
    }

}
