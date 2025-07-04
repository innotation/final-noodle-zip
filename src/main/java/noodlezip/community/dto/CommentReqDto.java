package noodlezip.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import noodlezip.common.validation.ValidationGroups;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReqDto {
    @NotNull(message = "ID 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    private Long boardId;
    @NotNull(message = "내용은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @NotEmpty(message = "내용은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @NotBlank(message = "내용은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    private String content;

    private Long userId;
}
