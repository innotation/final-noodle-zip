package noodlezip.ocr.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.ocr.service.OcrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/receipt")
@Slf4j
public class OcrController {

    private final OcrService ocrService;

    @GetMapping("/auth/check")
    @ResponseBody
    public ResponseEntity<String> checkLogin(@AuthenticationPrincipal MyUserDetails user) {
        if (user == null || user.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }
        return ResponseEntity.ok("로그인됨");
    }

    @PostMapping
    public ResponseEntity<?> uploadReceipt(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = ocrService.analyzeAndPrepareResponse(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("error", "인식에 실패했습니다. 영수증을 다시 업로드 해주세요.");
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping
    public String showOcrForm() {
        return "/receipt";
    }

}

