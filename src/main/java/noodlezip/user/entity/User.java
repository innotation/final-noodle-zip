package noodlezip.user.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;

import java.util.Date;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "birth", nullable = false)
    private String birth;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "profile_banner_image_url")
    private String profileBannerImageUrl;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false)
    private ActiveStatus activeStatus;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;
}
