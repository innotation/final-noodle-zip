package noodlezip.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import noodlezip.common.validation.ValidationGroups;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = "ID 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @NotBlank(message = "ID 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    private String loginId;

    @NotNull(message = "비밀번호 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class} )
    @NotBlank(message = "비밀번호 값은 비어있을 수 없습니다.", groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자, 영문, 숫자, 특수문자를 포함해야 합니다.", groups = {ValidationGroups.OnCreate.class})
    private String password;

    @NotNull(message = "이름은 비어있을 수 없습니다.", groups = {ValidationGroups.OnCreate.class})
    @NotBlank(message = "이름은 비어있을 수 없습니다.", groups = {ValidationGroups.OnCreate.class})
    private String userName;;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|1[1-9]|2[1-9]|3[0-1])$"
            , message = "생년월일은 YYYYMMDD 8자리 형식으로 입력해주세요.", groups = {ValidationGroups.OnCreate.class})
    private String birth;

    @NotNull(message = "이메일 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @NotBlank(message = "이메일 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
    , message = "이메일 형식에 맞게 입력해주세요.", groups = {ValidationGroups.OnCreate.class})
    private String email;

    @NotNull(message = "휴대폰 번호 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @NotBlank(message = "휴대폰 번호 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}"
    , message = "휴대폰 번호는 012-3456-7890 형식으로 입력해주세요.", groups = {ValidationGroups.OnCreate.class})
    private String phone;

    @NotNull(message = "성별 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    @NotBlank(message = "성별 값은 비어있을 수 없습니다", groups = {ValidationGroups.OnCreate.class})
    private String gender;
}
