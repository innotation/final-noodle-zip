package noodlezip.mypage.controller;

import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.exception.CustomException;
import noodlezip.mypage.constant.MyPageUrlPolicy;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.mypage.exception.MyPageErrorStatus;
import noodlezip.user.entity.User;

public abstract class MyBaseController {

    protected UserAccessInfo resolveUserAccess(MyUserDetails userDetails, Long targetUserId) {
        if (userDetails == null) {
            return new UserAccessInfo(targetUserId, null, false);
        }

        User user = userDetails.getUser();
        Long requestUserId = user.getId();

        if (requestUserId.equals(targetUserId)) {
            return new UserAccessInfo(requestUserId, requestUserId, true);
        } else {
            return new UserAccessInfo(targetUserId, requestUserId, false);
        }
    }

}
