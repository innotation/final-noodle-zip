package noodlezip.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import noodlezip.common.validation.ValidationGroups;

@Getter
@Setter
@ToString
public class BoardReqDto {

    @NotNull(message = "제목은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class} )
    @NotBlank(message = "제목은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class} )
    private String title;

    @NotNull(message = "내용은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class} )
    @NotBlank(message = "내용은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class} )
    private String content;
}
