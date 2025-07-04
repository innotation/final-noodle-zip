package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.exception.CustomException;
import noodlezip.mypage.dto.response.SavedStoreCategoryFilterResponse;
import noodlezip.mypage.exception.MyPageErrorStatus;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class MySavedStoreController {
    /**
     * 저장 가게 카테고리 목록 조회 - userId
     * 저장가게전체 조회            - userId(페이지네이션)
     * 선택 카테고리 조회           - userId + categoryId(페이지네이션)
     * 좌표 조회                    - userId + categoryId
     * 선택좌표상세보기조회(경배님이만든거) - storeId (근데 메모 띄우려면 savedStoreId에서 store조인헤야겠ㄸ니
     */

    @GetMapping("/my/saved-store/list")
    /// 사용자가 본인의 마이페이지에 접근해야만 들어올 수 있음
    public String mySavedStoreList(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        User user = userDetails.getUser();

        /**
         * 처음에 조회할때떄는
         * 1. 카테고리 목록 필터 - SavedStoreCategoryResponse
         * 2. 조회 결과 페이지네이션 결과 - 페이지네이션정보 + List\<저장가게정보>
         *
         *     카테고리 목록 조회는 좌표 찍을떄도 필요하기 때문에 따로 보내는게 좋은 거 같다.(지도보기 누르면 SavedStoreCategoryResponse 보내야됨)
         */
        model.addAttribute("userId", user.getId());
        model.addAttribute("isOwner", true);
        return "index";
    }

    @GetMapping("/{userId}/saved-store/list")
    public String savedStoreList(@PathVariable Long userId, Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("isOwner", false);
        return "index";
    }


    /// 이때는 사용자가 검색 기능을 눌렀을때 + 처음 조회 페이지에서 페이지 넘겼을 때 ㅍ리터 비동기 조회
    @GetMapping("/{userId}/saved-store/list/category-filter-search") //내 조회, 다른사람 조회 같이 사용 - 어차피 비동기
    @ResponseBody
    public void getMySavedStoreListByCategory(@AuthenticationPrincipal MyUserDetails userDetails,
                                              @PathVariable Long userId,
                                              @ModelAttribute SavedStoreCategoryFilterResponse categoryFilter
    ) {

        User user = userDetails.getUser();
        if(!user.getId().equals(userId)) {
            throw new CustomException(MyPageErrorStatus._NOT_FOUND_USER_MY_PAGE);
        }
    }
    
    
    /// 사용자가 지도보기를 눌렀을때  storeId+좌표 리스트 비동기 조회
    @GetMapping("/{userId}/saved-store/list/map")
    @ResponseBody
    public void getMySavedStoreListMap(@AuthenticationPrincipal MyUserDetails userDetails,
                                       @PathVariable Long userId,
                                       @ModelAttribute SavedStoreCategoryFilterResponse categoryFilter
    ) {
        User user = userDetails.getUser();
        if(!user.getId().equals(userId)) {
            throw new CustomException(MyPageErrorStatus._NOT_FOUND_USER_MY_PAGE);
        }
    }
    
    
    


}
