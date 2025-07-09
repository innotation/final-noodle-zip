package noodlezip.ocr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import noodlezip.ocr.service.OcrService;
import noodlezip.ocr.util.OcrUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/receipt")
public class OcrController {

    private final OcrService ocrService;

    @PostMapping
    public ResponseEntity<?> uploadReceipt(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = ocrService.analyzeAndPrepareResponse(file);
            System.out.println(result);
            System.out.println(result.get("ocrKeyHash"));
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

