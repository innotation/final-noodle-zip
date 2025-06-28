package noodlezip.badge.service.reader.review;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.Region;
import noodlezip.badge.dto.RamenReviewBadgeDto;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import noodlezip.badge.service.process.level.RamenReviewCategoryBadge;
import noodlezip.badge.service.reader.BadgeEventReader;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RamenReviewPostEvent implements BadgeEventReader<RamenReviewBadgeDto> {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final LevelDirectUpdateProcessor directUpdateProcessor;
    private final RamenReviewCategoryBadge ramenReviewCategoryBadge;

    @Override
    public void read(Long userId, RamenReviewBadgeDto extraOption) {
        processAllCommunityCount(userId);
        processRamenCategory(userId, extraOption.getMenuIdList());
        processRamenStoreRegion(userId, extraOption.getStoreId());
    }


    public void processAllCommunityCount(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.ALL_COMMUNITY_POST_COUNT_BADGE);
    }

    /// 라멘 카테고리 리스트 필요
    private void processRamenCategory(Long userId, List<Long> menuIdList) {
        // 메뉴 아이디 리스트 -> 라멘 카테고리 아이디 알아오기 -> 리스트 반목문으로 배지 로직에 전달

    }

    /// 지역 코드 필요
    private void processRamenStoreRegion(Long userId, Long storeId) {
        // 스토어 아이디 -> 법정코드 알아오기 int -> 지역코드 추출 -> 배지 로직에 전달
        Region region = Region.getRegionByCode(11111);
        if (region == null) {
            return;
        }
        int sidoRegionCode = region.getCode();

    }

}
