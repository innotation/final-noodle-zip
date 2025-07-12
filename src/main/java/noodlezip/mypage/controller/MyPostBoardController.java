package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class MyPostBoardController extends MyBaseController {

//    @GetMapping("/{userId}/boards")
//    public String getMyPostBoards(@AuthenticationPrincipal MyUserDetails myUserDetails,
//                                  @PathVariable Long userId,
//                                  @PageableDefault(
//                                          size = 6,
//                                          sort = "id",
//                                          direction = Sort.Direction.DESC
//                                  ) Pageable pageable,
//                                  Model model
//    ) {
//        pageable = pageable.withPage(pageable.getPageNumber() <= 0
//                ? 0
//                : pageable.getPageNumber() - 1);
//
//
//
//    }
}
