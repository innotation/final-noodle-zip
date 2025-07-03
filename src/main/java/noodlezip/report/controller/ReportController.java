package noodlezip.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.report.dto.ReportDto;
import noodlezip.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/report")
    @ResponseBody
    public String submitReport(
            @ModelAttribute ReportDto dto,
            @RequestParam(required = false) List<String> reasons,
            @RequestParam(required = false) String content
    ) {
        // 여러 체크박스 사유 + 추가 텍스트를 content로 합침
        StringBuilder finalContent = new StringBuilder();

        if (reasons != null && !reasons.isEmpty()) {
            finalContent.append(String.join(", ", reasons));
        }

        if (content != null && !content.trim().isEmpty()) {
            if (finalContent.length() > 0) finalContent.append("\n");
            finalContent.append(content.trim());
        }

        dto.setContent(finalContent.toString());
        reportService.save(dto);
        return "OK";
    }
}

