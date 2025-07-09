package noodlezip.mypage.controller;

import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.exception.CustomException;
import noodlezip.mypage.constant.MyPageUrlPolicy;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.mypage.exception.MyPageErrorStatus;
import noodlezip.user.entity.User;

public abstract class MyBaseController {

    protected UserAccessInfo resolveUserAccess(MyUserDetails userDetails, String userId) {
        User user = userDetails.getUser();

        if (userId == null || MyPageUrlPolicy.MY_PAGE_KEY.equals(userId)) {
            return new UserAccessInfo(user.getId(), true);
        } else {
            try {
                return new UserAccessInfo(Long.valueOf(userId), false);
            } catch (NumberFormatException e) {
                throw new CustomException(MyPageErrorStatus._NOT_FOUND_USER_MY_PAGE);
            }
        }
    }

}
