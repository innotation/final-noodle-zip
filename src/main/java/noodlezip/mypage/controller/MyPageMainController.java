package noodlezip.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.mypage.dto.response.MyPageResponse;
import noodlezip.user.entity.User;
import noodlezip.user.service.UserService;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.service.BoardService;
import noodlezip.user.status.UserErrorStatus;
import noodlezip.common.exception.CustomException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.jsoup.Jsoup;
import noodlezip.badge.service.MyBadgeService;
import noodlezip.badge.dto.response.UserBadgeResponse;
import noodlezip.subscription.repository.UserSubscriptionRepository;
import noodlezip.badge.constants.Region;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
@Tag(name = "마이페이지", description = "미이페이지 연동 API")
public class MyPageMainController extends MyBaseController {
    private final UserService userService;
    private final BoardService boardService;
    private final ModelMapper modelMapper;
    private final noodlezip.ramen.service.RamenService ramenService;
    private final MyBadgeService myBadgeService;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Operation(
            summary = "마이페이지 메인페이지 정보 조회",
            description = "특정 사용자의 마이페이지 정보를 조회합니다."
    )
    @Parameters({
            @Parameter(name = "userId", description = "조회될 사용자 PK"),
            @Parameter(name = "myUserDetails", hidden = true),
            @Parameter(name = "model", hidden = true)
    })
    @GetMapping("/{userId}")
    public String userMyPage(@AuthenticationPrincipal MyUserDetails myUserDetails,
                             @PathVariable Long userId,
                             Model model
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        // 유저 정보 조회
        User user = userService.findExistingUserByUserId(userId)
            .orElseThrow(() -> new CustomException(UserErrorStatus._NOT_FOUND_USER));
        MyPageResponse myPage = MyPageResponse.builder()
            .id(user.getId())
            .userName(user.getUserName())
            .profileImageUrl(user.getProfileImageUrl())
            .profileBannerImageUrl(user.getProfileBannerImageUrl())
            .build();
        // 유저 게시글 리스트 조회
        Pageable pageable = PageRequest.of(0, 100); // 1페이지, 100개
        @SuppressWarnings("unchecked")
        java.util.List<BoardRespDto> boards = (java.util.List<BoardRespDto>) boardService.findBoardByUser(userId, pageable).get("list");
        for (BoardRespDto board : boards) {
            board.setPlainContent(org.jsoup.Jsoup.parse(board.getContent()).text());
        }
        // 내가 쓴 리뷰 리스트 조회
        java.util.List<noodlezip.store.dto.StoreReviewDto> myReviews = ramenService.findReviewsByUserId(userId, pageable).getContent();

        // 총 리뷰 개수
        int totalReviewCount = myReviews.size();

        // 방문 지역별 리뷰 수 집계
        Map<String, Integer> visitedRegionCountMap = new HashMap<>();
        for (noodlezip.store.dto.StoreReviewDto review : myReviews) {
            Long legalCode = review.getStoreLegalCode();
            if (legalCode != null) {
                int regionCode = Integer.parseInt(legalCode.toString().substring(0, 2));
                Region region = Region.getRegionBySidoCode(regionCode);
                String regionName = (region != null) ? region.getName() : "기타";
                visitedRegionCountMap.put(regionName, visitedRegionCountMap.getOrDefault(regionName, 0) + 1);
            }
        }
        // 방문횟수 내림차순 정렬
        Map<String, Integer> sortedRegionCountMap = visitedRegionCountMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                java.util.LinkedHashMap::new
            ));

        // 좋아하는 라멘 카테고리(내가 쓴 리뷰의 categoryName 기준) 집계 및 내림차순 정렬
        Map<String, Integer> favoriteCategoryCountMap = myReviews.stream()
            .filter(r -> r.getCategoryName() != null && !r.getCategoryName().isEmpty())
            .collect(java.util.stream.Collectors.groupingBy(
                noodlezip.store.dto.StoreReviewDto::getCategoryName,
                java.util.stream.Collectors.summingInt(r -> 1)
            ));
        Map<String, Integer> sortedFavoriteCategoryCountMap = favoriteCategoryCountMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                java.util.LinkedHashMap::new
            ));
        // 뱃지 3개 추천 조회
        java.util.List<UserBadgeResponse> userBadges = myBadgeService.getUserBadgeForMyPageProfile(userId);
        if (userBadges.size() > 3) userBadges = userBadges.subList(0, 3);
        model.addAttribute("userBadges", userBadges);
        model.addAttribute("userAccessInfo", userAccessInfo);
        model.addAttribute("myPage", myPage);
        model.addAttribute("boards", boards);
        model.addAttribute("myReviews", myReviews);
        // 팔로우/팔로워 수 조회 및 모델에 추가
        long followingCount = userSubscriptionRepository.countByFollowerId(userId); // 내가 팔로우하는 사람 수
        long followerCount = userSubscriptionRepository.countByFolloweeId(userId); // 나를 팔로우하는 사람 수
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        // 총 리뷰수 모델에 추가
        model.addAttribute("totalReviewCount", totalReviewCount);
        // 방문 지역별 리뷰수 모델에 추가
        model.addAttribute("visitedRegionCountMap", sortedRegionCountMap);
        // 좋아하는 라멘 카테고리별 리뷰수 모델에 추가
        model.addAttribute("favoriteCategoryCountMap", sortedFavoriteCategoryCountMap);
        return "mypage/main";
    }

    /**
     * 마이페이지 메인 페이지        /mypage/{userId}
     * 배지 조회                  /users/{userId}/badges
     * 저장가게 조회               /users/{userId}/saved-stores
     * 구독 목록 조회              /users/{userId}/follower
     * 내가 쓴 게시글 조회 :        /users/{userId}/boards
     * 내가 좋아요한 게시글 조회 :    /users/{userId}/liked-boards
     * 내가 단 댓글 조회 :          /users/{userId}/comments
     */

}
