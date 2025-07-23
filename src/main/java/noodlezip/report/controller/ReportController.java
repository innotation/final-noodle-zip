package noodlezip.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.report.dto.ReportRequestDto;
import noodlezip.report.service.ReportService;
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
            @ModelAttribute ReportRequestDto dto,
            @RequestParam(required = false) List<String> reasons,
            @RequestParam(required = false) String content
    ) {

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

