package noodlezip.user.service;

import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void registUser(User user);
    boolean isLoginIdDuplicated(String loginId);
    boolean isEmailDuplicated(String email);
    void verifyUserEmail(String email, String code);
    void updateUser(Long id, UserDto user, MultipartFile profileImage, MultipartFile bannerImage);
    void signoutUser(Long userId);
    void validateMyPageExistingUserByUserId(Long userId);
}
