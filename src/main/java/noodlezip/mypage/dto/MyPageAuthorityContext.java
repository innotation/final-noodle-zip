package noodlezip.mypage.dto;

import lombok.*;
import noodlezip.mypage.constant.MyPageUrlPolicy;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyPageAuthorityContext {

    private Long userId;
    private Boolean isOwner;
    private String dataPath;

    public MyPageAuthorityContext(Long userId, Boolean isOwner) {
        this.userId = userId;
        this.isOwner = isOwner;

        if (isOwner) {
            this.dataPath = MyPageUrlPolicy.MY_PAGE_KEY;
        } else {
            this.dataPath = userId.toString();
        }
    }

}
