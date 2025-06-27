package noodlezip.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 30)
    @NotNull
    @Column(name = "user_name", nullable = false, length = 30)
    private String userName;

    @Column(name = "birth")
    private Integer birth;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 10)
    @Column(name = "gender", length = 10)
    private String gender;

    @Size(max = 500)
    @Column(name = "profile_banner_image_url", length = 500)
    private String profileBannerImageUrl;

    @Size(max = 500)
    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Size(max = 10)
    @NotNull
    @Column(name = "user_type", nullable = false, length = 10)
    private String userType;

    @Size(max = 30)
    @NotNull
    @Column(name = "active_status", nullable = false, length = 30)
    private String activeStatus;

    @NotNull
    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified = false;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}