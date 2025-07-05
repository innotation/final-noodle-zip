package noodlezip.badge.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.exception.BadgeErrorStatus;
import noodlezip.badge.repository.BadgeRepository;
import noodlezip.common.exception.CustomException;
import noodlezip.mypage.dto.response.badge.LevelBadgeDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LevelBadgeDetailResponse> getNoOptionBadgeDetails(long userId, long badgeCategoryId) {
        List<LevelBadgeDetailResponse> result = badgeRepository.findNoOptionBadgeDetails(userId, badgeCategoryId);
        if (result == null || result.isEmpty()) {
            throw new CustomException(BadgeErrorStatus._NOT_FOUNT_USER_BADGE);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LevelBadgeDetailResponse> getOptionBadgeDetails(long userId,
                                                                long badgeId,
                                                                long badgeCategoryId
    ) {
        List<LevelBadgeDetailResponse> result =
                badgeRepository.findOptionBadgeDetails(userId, badgeId, badgeCategoryId);
        if (result == null || result.isEmpty()) {
            throw new CustomException(BadgeErrorStatus._NOT_FOUNT_USER_BADGE);
        }
        return result;
    }

}
