package noodlezip.savedstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.dto.ApiResponse;
import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;
import noodlezip.savedstore.dto.response.SavedStoreApiResponse;
import noodlezip.savedstore.service.SavedStoreCategoryService;
import noodlezip.savedstore.status.SavedStoreSuccessStatus;
import noodlezip.user.entity.User;
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

    @PostMapping(value = "/my/saved-store/categories/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SavedStoreApiResponse updateCategoryList(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                                    @RequestBody @Valid List<@Valid SavedStoreCategoryUpdateRequest> requestList) {
        User user = myUserDetails.getUser();
        saveStoreCategoryService.updateSavedCategoryList(user.getId(), requestList);


        for(SavedStoreCategoryUpdateRequest updateRequest : requestList) {
            System.out.println(updateRequest);
        }
        return SavedStoreApiResponse.builder()
                .success(true)
                .message(SavedStoreSuccessStatus._OK_UPDATE_SAVED_STORE_CATEGORY.getMessage())
                .build();
    }

    @PostMapping(value = "/my/saved-store/categories/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SavedStoreApiResponse deleteCategoryList(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                                    @RequestBody List<Long> categoryIdList) {
        User user = myUserDetails.getUser();
        saveStoreCategoryService.deleteSavedCategoryList(user.getId(), categoryIdList);

        return SavedStoreApiResponse.builder()
                .success(true)
                .message(SavedStoreSuccessStatus._OK_DELETED_SAVED_STORE_CATEGORY.getMessage())
                .build();
    }

}
