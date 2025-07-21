package noodlezip.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.admin.dto.RegistListDto;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.report.dto.ReportResponseDto;
import noodlezip.report.dto.ReportedCommentDto;
import noodlezip.report.service.ReportService;
import noodlezip.report.status.ReportStatus;
import noodlezip.store.service.StoreService;
import noodlezip.store.status.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final StoreService storeService;
    private final PageUtil pageUtil;
    private final ReportService reportService;

    @GetMapping("/login")
    public String showAdminLoginPage() {
        return "admin/login";
    }

    @GetMapping("/main")
    public void mainPage(){}

    @GetMapping("/reportList")
    public String reportList(@PageableDefault(size = 5) Pageable pageable,
                             @RequestParam(defaultValue = "ALL") String type,
                             Model model) {
        return "admin/reportList";
    }

    @GetMapping("/reportList/data")
    @ResponseBody
    public Map<String, Object> reportListData(
            @PageableDefault(size = 5) Pageable pageable,
            @RequestParam(defaultValue = "ALL") String type
    ) {
        return reportService.findReportList(pageable, type);
    }

    @GetMapping("/report/post-category/{id}")
    @ResponseBody
    public ResponseEntity<String> getPostCategory(@PathVariable Long id) {
        try {
            String category = reportService.findPostCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (CustomException e) {
            log.warn("게시글 카테고리 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }

    // 댓글 신고 상세 조회 (비동기, Modal용)
    @GetMapping("/report/comment/{id}")
    @ResponseBody
    public ReportedCommentDto getReportedCommentById(@PathVariable Long id) {
        return reportService.getReportedCommentById(id);
    }

    // 신고 상태 변경 처리
    @PutMapping("/report/{id}/status")
    public ResponseEntity<Void> updateReportStatus(@PathVariable Long id,
                                                   @RequestParam("status") ReportStatus status) {
        reportService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/registList")
    public String registListPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        return "admin/registList";
    }

    @GetMapping("/registList/data")
    @ResponseBody
    public Map<String, Object> registListData(@PageableDefault(size = 5) Pageable pageable) {
        return storeService.findWaitingStores(pageable);
    }

    // 매장 상태 변경 처리 (예: 처리완료, 반려)
    @PutMapping("/regist/{id}/status")
    public ResponseEntity<Void> updateStoreStatus(@PathVariable Long id,
                                                   @RequestParam("status") ApprovalStatus status) {
        storeService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }



}
