package noodlezip.admin.dto;

import lombok.*;
import noodlezip.store.constant.ApprovalStatus;
import noodlezip.user.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistListDto {

    private String loginId;
    private String storeName;
    private String createdAt;
}
