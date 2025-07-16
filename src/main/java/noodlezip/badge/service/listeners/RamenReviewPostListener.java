package noodlezip.badge.service.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.Region;
import noodlezip.badge.constants.UserEventType;
import noodlezip.badge.events.RamenReviewBadgeEvent;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import noodlezip.badge.service.process.level.RamenReviewCategoryBadge;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class RamenReviewPostListener {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final LevelDirectUpdateProcessor directUpdateProcessor;
    private final RamenReviewCategoryBadge ramenReviewCategoryBadge;

    @Async
    @TransactionalEventListener
    public void processAllCommunityCount(RamenReviewBadgeEvent event) {
        try {
            directUpdateProcessor.process(
                    event.getUserId(), LevelBadgeCategoryType.ALL_COMMUNITY_POST_COUNT_BADGE);
        } catch (Exception e) {
            log.error("[BadgeFail] userId={} event={} badgeType={} reason={}",
                    event.getUserId(),
                    UserEventType.RAMEN_REVIEW_POST.name(),
                    LevelBadgeCategoryType.ALL_COMMUNITY_POST_COUNT_BADGE.name(),
                    e.getMessage(), e
            );
        }
    }

    @Async
    @TransactionalEventListener
    public void processRamenCategory(RamenReviewBadgeEvent event) {
        // 메뉴 아이디 리스트 -> 라멘 카테고리 아이디 알아오기 -> 리스트 반목문으로 배지 로직에 전달

    }

    @Async
    @TransactionalEventListener
    public void processRamenStoreRegion(RamenReviewBadgeEvent event) {
        // 스토어 아이디 -> 법정코드 알아오기 int -> 지역코드 추출 -> 배지 로직에 전달
        Region region = Region.getRegionByCode(11111);
        if (region == null) {
            return;
        }
    }

}
