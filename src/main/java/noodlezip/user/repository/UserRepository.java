package noodlezip.user.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import noodlezip.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(@NotNull(message = "이메일 값은 비어있을 수 없습니다") @NotBlank(message = "이메일 값은 비어있을 수 없습니다") @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
    , message = "이메일 형식에 맞게 입력해주세요.") String email);
}
