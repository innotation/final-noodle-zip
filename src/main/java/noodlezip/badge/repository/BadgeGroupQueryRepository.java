package noodlezip.badge.repository;

import noodlezip.badge.dto.response.BadgeGroupResponse;

import java.util.List;

public interface BadgeGroupQueryRepository {

    List<BadgeGroupResponse> getBadgeGroups();

}
