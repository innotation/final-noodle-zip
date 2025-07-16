package noodlezip.ramen.repository;

import noodlezip.ramen.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
    Optional<Topping> findByToppingName(String toppingName); // 이미 존재하는 토핑이면 재사용, 없으면 생성
}