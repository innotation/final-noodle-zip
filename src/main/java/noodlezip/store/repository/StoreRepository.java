package noodlezip.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAll();

}