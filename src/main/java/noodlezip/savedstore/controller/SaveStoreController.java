package noodlezip.savedstore.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.service.SavedStoreService;
import noodlezip.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import noodlezip.savedstore.dto.request.AddCategoryRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stores")
@Controller
public class SaveStoreController {

    private final SavedStoreService saveStoreService;


    /// FIXME 가게 상세보기 컨트롤러에 두는게 맞다 /// Wishlist 버튼 누를떄
    @GetMapping(value = "/{storeId}/saved-store/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SavedStoreCategoryResponse> getSavedStoreInfo(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                                              @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        return saveStoreService.getUserSaveCategoryList(user.getId(), storeId);
    }

    /// 위시리스트 버튼 활성화 여부 확인
    @GetMapping(value = "/{storeId}/saved-store/check", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean checkSavedStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                   @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        return saveStoreService.isSavedStore(user.getId(), storeId);
    }


    /// 저장 모달에서 저장하기 버튼을 누를 때
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{storeId}/saved-store/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void savedStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                           @PathVariable Long storeId,
                           @RequestBody @Validated SaveStoreRequest saveStoreRequest
    ) {
        User user = myUserDetails.getUser();
        saveStoreService.saveSavedStore(user.getId(), storeId, saveStoreRequest);
    }


    /// 저장 버튼을 삭제할때
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{storeId}/saved-store/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                            @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        saveStoreService.deleteSavedStore(user.getId(), storeId);
    }

    /// 새 카테고리 추가 (위시리스트 모달에서 사용)
    @PostMapping(value = "/{storeId}/saved-store/add-category", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void addSavedStoreCategory(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                      @PathVariable Long storeId,
                                      @RequestBody AddCategoryRequest addCategoryRequest) {
        User user = myUserDetails.getUser();
        saveStoreService.addSavedStoreCategory(user.getId(), addCategoryRequest);
    }
}
