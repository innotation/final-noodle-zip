package noodlezip.savedstore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;
import noodlezip.savedstore.dto.response.SavedStoreApiResponse;
import noodlezip.savedstore.service.SavedStoreCategoryService;
import noodlezip.savedstore.status.SavedStoreSuccessStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class SaveStoreCategoryController {

    private final SavedStoreCategoryService saveStoreCategoryService;

    /**
     * 카테고리를 수정하고 삭제하는 경로의 유입은 모직 자신의 페이지에서만 행해지는것이기는 하지만
     * @AuthenticationPrincipal로 받아서 지금 요청한 사용자의 저장가게카테고리아이디가 맞는지 한번 더 확인해봐야하나?
     *
     * -> 검증 해야한다. 보내진 카테고리의 userId가 요청한 user와 동일한지 검증해야한다.
     */
    @PostMapping(
            value = "/my/saved-store/categories/update",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public SavedStoreApiResponse updateCategoryList(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                                    @RequestBody @Validated List<SavedStoreCategoryUpdateRequest> requestList) {
        saveStoreCategoryService.updateSavedCategoryList(requestList);

        return SavedStoreApiResponse.builder()
                .isSuccess(true)
                .message(SavedStoreSuccessStatus._OK_UPDATE_SAVED_STORE_CATEGORY.getMessage())
                .build();
    }


    @PostMapping(
            value = "/my/saved-store/categories/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public SavedStoreApiResponse deleteCategory(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                                @RequestBody List<Long> categoryIdList) {
        saveStoreCategoryService.deleteSavedCategoryList(categoryIdList);

        return SavedStoreApiResponse.builder()
                .isSuccess(true)
                .message(SavedStoreSuccessStatus._OK_DELETED_SAVED_STORE_CATEGORY.getMessage())
                .build();
    }

}
