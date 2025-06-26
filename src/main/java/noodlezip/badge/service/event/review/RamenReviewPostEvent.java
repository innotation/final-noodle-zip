package noodlezip.badge.service.event.review;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.Region;
import noodlezip.badge.dto.RamenReviewBadgeDto;
import noodlezip.badge.service.category.AllCommunityPostCountBadge;
import noodlezip.badge.service.event.BadgeEventReader;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RamenReviewPostEvent implements BadgeEventReader<RamenReviewBadgeDto> {

    private final AllCommunityPostCountBadge allCommunityPostCountBadge;

    @Override
    public void read(Long userId, RamenReviewBadgeDto extraOption) {
        int legalCode = getRegalCode(extraOption.getStoreId());

        allCommunityPostCountBadge.process(userId);
    }

    private void processRamenStoreRegion(Long userId, int storeLegalCode) {
        /**
         * 1. 지역 파싱
         * 2. user_id + badge_category_id + 지역 같은거 있는지 확인
         * 3. 나머지 똑같이 연산
         * current_score하고 total_value도 누적 저장해야함
         */


    }

    private void processRamenCategory(Long userId, int ramenCategoryId) {

    }

    public int getRegalCode(Long storeId) {
        return Region.getRegionByCode(11111).getCode();
    }


}
