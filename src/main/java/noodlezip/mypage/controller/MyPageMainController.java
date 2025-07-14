package noodlezip.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
@Tag(name = "마이페이지", description = "미이페이지 연동 API")
public class MyPageMainController extends MyBaseController {

    private final StoreService storeService;

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
        // 본인 확인
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        model.addAttribute("userAccessInfo", userAccessInfo);

        // 내가 등록한 매장 목록 조회
        Long loginUserId = myUserDetails.getUser().getId();

        List<StoreDto> myStores = storeService.getStoresByUserId(loginUserId);
        model.addAttribute("myStores", myStores);

        return "index";
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
    @DeleteMapping("/store/delete/{storeId}")
    @ResponseBody
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId,
                                         @AuthenticationPrincipal MyUserDetails userDetails) {
        Long loginUserId = userDetails.getUser().getId();

        // storeService에 폐업처리 메서드가 있다고 가정
        try {
            storeService.markStoreAsClosed(storeId, loginUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("폐업 처리 실패", e);
            return ResponseEntity.status(500).body("폐업 처리 중 오류 발생");
        }
    }

}
