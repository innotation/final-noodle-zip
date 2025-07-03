package noodlezip.user.service;

import noodlezip.user.dto.UserDto;

public interface UserService {
    void registUser(UserDto user);
    boolean isLoginIdDuplicated(String loginId);
    boolean isEmailDuplicated(String email);
    void verifyUserEmail(String email, String code);

    void validateMyPageExistingUserByUserId(Long userId);
}
