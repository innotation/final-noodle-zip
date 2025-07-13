package noodlezip.badge.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.Region;
import noodlezip.badge.dto.UserNoOptionBadgeDto;
import noodlezip.badge.dto.UserOptionBadgeDto;
import noodlezip.badge.dto.response.*;
import noodlezip.badge.entity.*;
import noodlezip.badge.status.BadgeErrorStatus;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.ramen.entity.Category;
import noodlezip.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyBadgeServiceImpl implements MyBadgeService {

    private final UserService userService;
    private final BadgeCategoryService badgeCategoryService;
    private final BadgeService badgeService;
    private final UserBadgeService userBadgeService;

    @Override
    @Transactional(readOnly = true)
    public List<UserBadgeResponse> getUserBadgeForMyPageProfile(Long userId) {
        List<UserBadgeResponse> result = new ArrayList<>();
        List<UserBadge> userBadgeList = userBadgeService.getUserBadgeForMyPageProfile(userId);

        for (UserBadge userBadge : userBadgeList) {
            UserBadgeResponse userBadgeResponse = new UserBadgeResponse();

            Badge badge = userBadge.getBadge();
            BadgeCategory badgeCategory = badge.getBadgeCategory();

            userBadgeResponse.setUserBadgeId(userBadge.getId());
            userBadgeResponse.setBadgeId(badge.getId());

            if (badge.isOptionalBadge()) {
                BadgeExtraOption extraOption = badge.getBadgeExtraOption();
                String optionName = resolveOptionName(extraOption);
                userBadgeResponse.setBadgeTitleName(optionName);
            } else {
                userBadgeResponse.setBadgeTitleName(badgeCategory.getBadgeDescription());
            }
            if (badge.isLevelBadge()) {
                userBadgeResponse.setBadgeLevelName(badge.getBadgeName());
            }

            result.add(userBadgeResponse);
        }
        return result;
    }

    private String resolveOptionName(BadgeExtraOption extraOption) {
        if (extraOption != null) {
            Category ramenCategory = extraOption.getRamenCategory();
            if (ramenCategory != null) {
                return ramenCategory.getCategoryName();
            }

            Integer storeSidoLegalCode = extraOption.getStoreSidoLegalCode();
            if (storeSidoLegalCode != null) {
                Region region = Region.getRegionBySidoCode(storeSidoLegalCode);
                if (region == null) {
                    throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
                }
                return region.getName();
            }
        }
        throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyBadgeBadgeResponse> getUserBadgeListByGroup(Long userId) {
        userService.validateMyPageExistingUserByUserId(userId);

        List<BadgeGroupResponse> badgeGroupList = userBadgeService.getBadgeGroupList();
        List<UserNoOptionBadgeDto> notOptionBadgeList = userBadgeService.getNoOptionUserBadgeList(userId);
        List<UserOptionBadgeDto> optionBadgeList = userBadgeService.getOptionUserBadgeList(userId);

        notOptionBadgeList.sort(Comparator.comparing(UserNoOptionBadgeDto::getBadgeGroupId));
        optionBadgeList.sort(Comparator.comparing(UserOptionBadgeDto::getBadgeGroupId));

        return mergeBadgeListResultByGroup(badgeGroupList, notOptionBadgeList, optionBadgeList);
    }

    private List<MyBadgeBadgeResponse> mergeBadgeListResultByGroup(List<BadgeGroupResponse> badgeGroupList,
                                                                   List<UserNoOptionBadgeDto> notOptionBadgeList,
                                                                   List<UserOptionBadgeDto> optionBadgeList

    ) {
        List<MyBadgeBadgeResponse> result = new ArrayList<>();

        for (BadgeGroupResponse badgeGroupResponse : badgeGroupList) {
            List<UserBadgeResponse> mergedList = new ArrayList<>();
            Long groupId = badgeGroupResponse.getId();

            for (UserNoOptionBadgeDto noOption : notOptionBadgeList) {
                if (noOption.getBadgeGroupId().equals(groupId)) {
                    mergedList.add(UserBadgeResponse.builder()
                            .userBadgeId(noOption.getUserBadgeId())
                            .badgeId(noOption.getBadgeId())
                            .badgeCategoryId(noOption.getBadgeCategoryId())
                            .badgeGroupId(noOption.getBadgeGroupId())
                            .badgeTitleName(noOption.getBadgeCategoryName())
                            .badgeLevelName(noOption.getBadgeName())
                            .accumulativeValue(noOption.getAccumulativeValue())
                            .badgeImageUrl(noOption.getBadgeImageUrl())
                            .build());
                }
            }
            for (UserOptionBadgeDto option : optionBadgeList) {
                if (option.getBadgeGroupId().equals(groupId)) {
                    String badgeTitleName = decideOptionBadgeTitleName(
                            option.getStoreSidoLegalCode(),
                            option.getRamenCategoryName()
                    );
                    mergedList.add(UserBadgeResponse.builder()
                            .userBadgeId(option.getUserBadgeId())
                            .badgeId(option.getBadgeId())
                            .badgeCategoryId(option.getBadgeCategoryId())
                            .badgeGroupId(option.getBadgeGroupId())
                            .badgeTitleName(badgeTitleName)
                            .badgeLevelName(option.getBadgeName())
                            .accumulativeValue(option.getAccumulativeValue())
                            .badgeImageUrl(option.getBadgeImageUrl())
                            .build());
                }
            }
            result.add(
                    MyBadgeBadgeResponse.builder()
                            .badgeGroup(badgeGroupResponse)
                            .userBadgeList(mergedList)
                            .build()
            );
        }
        return result;
    }

    private String decideOptionBadgeTitleName(Integer storeSidoLegalCode, String ramenCategoryName) {
        if (storeSidoLegalCode == null) {
            return ramenCategoryName;
        } else if (ramenCategoryName == null) {
            Region region = Region.getRegionBySidoCode(storeSidoLegalCode);
            if (region == null) {
                throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
            }
            return region.getName();
        }
        throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
    }

    @Override
    @Transactional(readOnly = true)
    public BadgeDetailResponse getBadgeDetailList(Long userId, Long badgeId, Long badgeCategoryId) {
        BadgeDetailResponse result = new BadgeDetailResponse();

        BadgeCategory badgeCategory = badgeCategoryService.getBadgeCategoryById(badgeCategoryId);
        BadgeStrategyType badgeStrategy = badgeCategory.getBadgeStrategy();
        List<LevelBadgeDetailResponse> levelBadgeDetailList = handleBadgeDetail(
                badgeStrategy,
                userId,
                badgeId,
                badgeCategoryId
        );

        for (LevelBadgeDetailResponse levelBadgeDetailResponse : levelBadgeDetailList) {
            if (levelBadgeDetailResponse.getBadgeId().equals(badgeId)) {
                result.setMainImageUrl(levelBadgeDetailResponse.getImageUrl());
                result.setObtainedDate(levelBadgeDetailResponse.getObtainedDate());
                break;
            }
        }
        result.setMainTitle(badgeCategory.getBadgeDescription());
        if (levelBadgeDetailList.size() == 1) {
            result.setLevelBadgeDetailList(new ArrayList<>());
        } else {
            result.setLevelBadgeDetailList(levelBadgeDetailList);
        }
        return result;
    }

    private List<LevelBadgeDetailResponse> handleBadgeDetail(BadgeStrategyType badgeStrategy,
                                                             Long userId,
                                                             Long badgeId,
                                                             Long badgeCategoryId
    ) {
//        if(badgeStrategy.isSingle()){
//        }
        LevelBadgeCategoryType levelBadgeCategoryType = LevelBadgeCategoryType.getByPk(badgeCategoryId);
        if (levelBadgeCategoryType == null) {
            throw new CustomException(BadgeErrorStatus._NOT_FOUND_BADGE_CATEGORY);
        }
        if (levelBadgeCategoryType.isOptional()) {
            return badgeService.getOptionBadgeDetails(userId, badgeId, badgeCategoryId);
        }
        return badgeService.getNoOptionBadgeDetails(userId, badgeCategoryId);
    }

}
