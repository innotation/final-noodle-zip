package noodlezip.mypage.controller;

import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.util.MyPageUrlPolicy;
import noodlezip.mypage.util.UserAccessInfo;
import noodlezip.user.entity.User;

public abstract class MyBaseController {

    protected UserAccessInfo resolveUserAccess(MyUserDetails userDetails, String userId) {
        User user = userDetails.getUser();
        if (userId == null || MyPageUrlPolicy.MY_PAGE_KEY.equals(userId)) {
            return new UserAccessInfo(user.getId(), true);
        } else {
            return new UserAccessInfo(Long.valueOf(userId), false);
        }
    }

}
