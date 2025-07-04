package noodlezip.savedstore.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.service.SaveStoreService;
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

    private final SaveStoreService saveStoreService;

    @GetMapping(
            value = "/{storeId}/saved-store/info", /// 저장 버튼을 누를 때
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<SavedStoreCategoryResponse> getSavedStoreInfo(@AuthenticationPrincipal MyUserDetails myUserDetails, /// FIXME 가게 상세보기 컨트롤러에 두는게 맞다
                                                              @PathVariable String storeId
    ) {
        User user = myUserDetails.getUser();
        return saveStoreService.getUserSaveCategoryList(user.getId(), storeId);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(
            value = "/{storeId}/saved-store/save",  /// 저장 모달에서 저장하기 버튼을 누를 때
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void savedStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                           @PathVariable Long storeId,
                           @RequestBody @Validated SaveStoreRequest saveStoreRequest
    ) {
        User user = myUserDetails.getUser();
        saveStoreService.addSavedStore(user.getId(), storeId, saveStoreRequest);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(
            value = "/{storeId}/saved-store/delete", /// 저장 버튼을 삭제할때
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void deleteStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                            @PathVariable Long storeId
    ) {
        User user = myUserDetails.getUser();
        saveStoreService.deleteSavedStore(user.getId(), storeId);
    }

}
