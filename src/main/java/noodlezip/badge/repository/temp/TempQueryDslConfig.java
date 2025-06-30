package noodlezip.badge.repository.temp;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//fixme 임의로 생성한 쿼리DSL config 파일입니다. (common에 있으면 삭제할 예정)
@Configuration
public class TempQueryDslConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }

}
