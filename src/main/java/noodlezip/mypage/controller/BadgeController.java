package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.mypage.dto.response.MyBadgeBadgeResponse;
import noodlezip.mypage.service.BadgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class BadgeController {

    private final BadgeService myBadgeService;


//    @GetMapping("/my/badge")
//    public String badge(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) { /// 본인지
//        User user = myUserDetails.getUser();
//        List<MyBadgeBadgeResponse> userBadgeList = myBadgeService.getMyBadgeListByGroup(user.getId());
//
//        model.addAttribute("userBadgeList", userBadgeList);
//        log.info("{}", userBadgeList);
//        return "mypage/badge";
//    }

    /**
     * 마이페이지에 진입하면 @AuthenticationPrincipal MyUserDetails로 받아서 MODEL에 ID 넘긴 후
     * 배지 페이지, 조회 페이지 등등응로 다 URL만들어준다ㅓ. /mypage/{userId}/badge
     */

    @GetMapping("/{userId}/badges/list")
    public String badge(@PathVariable Long userId, Model model) { /// 타인이 보는거랑 내가 보는거랑 목록에서는 차이가 없기떄문에 일단 띄운다(배지 상세)
        List<MyBadgeBadgeResponse> userBadgeList = myBadgeService.getUserBadgeListByGroup(userId);

        /// 만약 존재하지 않는 사용자의 경우에 404 보여줄지
        model.addAttribute("userBadgeList", userBadgeList);
        return "mypage/badge";
    }


    /// 동적으로 배지 상세 페이지 정보 보내기(user_id + badge_category_id)

    /// 동적으로 즐겨찾기 및 즐겨찾기 가능 여부 보내기  +  message, alert()

}
