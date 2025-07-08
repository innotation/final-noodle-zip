package noodlezip.badge.repository;

import noodlezip.badge.entity.BadgeGroup;
import noodlezip.mypage.dto.response.badge.BadgeGroupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BadgeGroupRepository extends JpaRepository<BadgeGroup, Long> {

    /// todo dsl로 작성
    @Query("""
             select new noodlezip.mypage.dto.response.badge.BadgeGroupResponse(bg.id, bg.badgeGroupName)
             from BadgeGroup bg
             order by bg.id asc
            """)
    List<BadgeGroupResponse> getBadgeGroups();

}