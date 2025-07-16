package noodlezip.mypage.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserAccessInfo {

    private Long targetUserId;   /// 조회 당하는 사람
    private Long requestUserId;  /// 조회 하는 사람
    private Boolean isOwner;     /// 조회 하는 사람 자신의 페이지인지에 대한 여부 (조회 당하는 사람 = 조회 하는 사람)

}
