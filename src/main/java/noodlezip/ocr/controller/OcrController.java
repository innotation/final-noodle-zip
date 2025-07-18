package noodlezip.ocr.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.dto.ApiResponse;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.ocr.dto.OcrRepDto;
import noodlezip.ocr.service.OcrService;
import noodlezip.ocr.status.OcrErrorStatus;
import noodlezip.ocr.status.OcrSuccessStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/receipt")
@Slf4j
public class OcrController {

    private final OcrService ocrService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> uploadReceipt(@RequestParam("file") MultipartFile file,
                                                             @AuthenticationPrincipal MyUserDetails user) {
        if (user == null || user.getUser() == null) {
            return ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED);
        }

        try {
            OcrRepDto result = ocrService.analyzeAndPrepareResponse(file);
            return ApiResponse.onSuccess(OcrSuccessStatus._OK_OCR_ANALYSIS, result);
        }

        catch (CustomException ce) {
            if (ce.getErrorCode() == OcrErrorStatus._HASH_KEY_GENERATION_FAIL ||
                    ce.getErrorCode() == OcrErrorStatus._PARSE_FAIL) {
                log.warn("OCR 인식 관련 예외: {}", ce.getMessage());
                return ApiResponse.onFailure(OcrErrorStatus._OCR_GENERAL_FAIL);
            }

            log.warn("OCR 사용자 예외: {}", ce.getMessage());
            return ApiResponse.onFailure(ce.getErrorCode());
        }

        // 시스템 에러
        catch (Exception e) {
            log.error("OCR 처리 중 서버 예외 발생", e);
            return ApiResponse.onFailure(OcrErrorStatus._FAIL_OCR_PROCESSING);
        }
    }

    @GetMapping
    public String showOcrForm() {
        return "receipt";
    }

}

