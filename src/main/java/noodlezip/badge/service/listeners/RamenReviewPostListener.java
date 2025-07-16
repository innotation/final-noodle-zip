package noodlezip.badge.service.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.Region;
import noodlezip.badge.constants.UserEventType;
import noodlezip.badge.events.RamenReviewBadgeEvent;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import noodlezip.badge.service.process.level.RamenReviewCategoryBadge;
import noodlezip.badge.service.process.level.RamenReviewSidoRegionBadge;
import noodlezip.badge.status.BadgeErrorStatus;
import noodlezip.common.exception.CustomException;
import noodlezip.store.service.MenuService;
import noodlezip.store.service.StoreService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RamenReviewPostListener {

    private final LevelDirectUpdateProcessor directUpdateProcessor;
    private final RamenReviewCategoryBadge ramenReviewCategoryBadge;
    private final RamenReviewSidoRegionBadge ramenReviewSidoRegionBadge;
    private final StoreService storeService;
    private final MenuService menuService;


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
        Long userId = event.getUserId();
        List<Long> menuIdList = event.getMenuIdList();

        try {
            if (menuIdList == null || menuIdList.isEmpty()) {
                log.error("[BadgeFail] 리뷰 메뉴 비어있음 userId={}", userId);
                return;
            }
            List<Integer> ramenCategoryIdList = menuService.getRamenCategoryIdListByMenuIds(menuIdList);

            for (Integer categoryId : ramenCategoryIdList) {
                ramenReviewCategoryBadge.process(userId, categoryId);
            }

        } catch (Exception e) {
            log.error("[BadgeFail] userId={} event={} badgeType={} reason={}",
                    event.getUserId(),
                    UserEventType.RAMEN_REVIEW_POST.name(),
                    LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.name(),
                    e.getMessage(), e
            );
        }
    }


    @Async
    @TransactionalEventListener
    public void processRamenStoreRegion(RamenReviewBadgeEvent event) {
        Long userId = event.getUserId();
        Long storeId = event.getStoreId();

        try {
            Long storeLegalCode = storeService.getStoreLegalCodeById(storeId);
            Region region = Region.getRegionByCode(storeLegalCode);
            if (region == null) {
                throw new CustomException(BadgeErrorStatus._NOT_FOUNT_REGION);
            }

            ramenReviewSidoRegionBadge.process(userId, region);

        } catch (Exception e) {
            log.error("[BadgeFail] userId={} event={} badgeType={} reason={}",
                    event.getUserId(),
                    UserEventType.RAMEN_REVIEW_POST.name(),
                    LevelBadgeCategoryType.RAMEN_REVIEW_REGION_BADGE.name(),
                    e.getMessage(), e
            );
        }
    }

}
