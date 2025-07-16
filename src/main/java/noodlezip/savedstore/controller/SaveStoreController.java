package noodlezip.savedstore.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.savedstore.dto.request.AddCategoryRequest;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stores")
@Controller
public class SaveStoreController {

    private final SavedStoreService saveStoreService;


    @GetMapping(value = "/{storeId}/saved-store/check", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean checkSavedStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                   @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        return saveStoreService.isSavedStore(user.getId(), storeId);
    }


    @GetMapping(value = "/{storeId}/saved-store/categoryList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SavedStoreCategoryResponse> getSavedStoreInfo(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                                              @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        return saveStoreService.getUserSaveCategoryList(user.getId(), storeId);
    }


    @GetMapping(value = "/{storeId}/saved-store/memo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSavedStoreMemo(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                    @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        return saveStoreService.getUserSavedStoreMemo(user.getId(), storeId);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{storeId}/saved-store/add-category", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addSavedStoreCategory(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                      @PathVariable Long storeId,
                                      @RequestBody AddCategoryRequest addCategoryRequest) {
        User user = myUserDetails.getUser();
        saveStoreService.addSavedStoreCategory(user.getId(), addCategoryRequest);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{storeId}/saved-store/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void savedStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                           @PathVariable Long storeId,
                           @RequestBody @Validated SaveStoreRequest saveStoreRequest
    ) {
        User user = myUserDetails.getUser();
        saveStoreService.saveSavedStore(user.getId(), storeId, saveStoreRequest);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{storeId}/saved-store/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                            @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        saveStoreService.deleteSavedStore(user.getId(), storeId);
    }

}
