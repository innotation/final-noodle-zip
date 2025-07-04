package noodlezip.savedstore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;
import noodlezip.savedstore.dto.response.SavedStoreApiResponse;
import noodlezip.savedstore.service.SaveStoreCategoryService;
import noodlezip.savedstore.status.SavedStoreSuccessStatus;
import org.springframework.http.MediaType;
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

    private final SaveStoreCategoryService saveStoreCategoryService;

    @PostMapping(
            value = "/my/saved-store/categories/update",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public SavedStoreApiResponse updateCategoryList(@RequestBody @Validated List<SavedStoreCategoryUpdateRequest> requestList) {
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
    public SavedStoreApiResponse deleteCategory(@RequestBody List<Long> categoryIdList) {
        saveStoreCategoryService.deleteSavedCategoryList(categoryIdList);

        return SavedStoreApiResponse.builder()
                .isSuccess(true)
                .message(SavedStoreSuccessStatus._OK_DELETED_SAVED_STORE_CATEGORY.getMessage())
                .build();
    }

}
