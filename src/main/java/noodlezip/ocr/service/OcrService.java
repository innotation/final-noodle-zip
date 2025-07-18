package noodlezip.ocr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.exception.CustomException;
import noodlezip.ocr.dto.OcrDataDto;
import noodlezip.ocr.dto.OcrRepDto;
import noodlezip.ocr.status.OcrErrorStatus;
import noodlezip.ocr.util.OcrUtil;
import noodlezip.ramen.repository.RamenReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrService {

    private final OcrUtil ocrUtil;
    private final OcrParser ocrParser;
    private final RamenReviewRepository reviewRepository;

    public OcrRepDto analyzeAndPrepareResponse(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("OCR 사용자 예외: 빈 파일이 업로드됨");
            throw new CustomException(OcrErrorStatus._EMPTY_RECEIPT_FILE);
        }

        String ocrJson;
        try {
            ocrJson = ocrUtil.process(file);
        } catch (Exception e) {
            log.error("OCR 이미지 처리 실패", e);
            throw new CustomException(OcrErrorStatus._FAIL_OCR_PROCESSING);
        }

        String hashKey;
        try {
            hashKey = ocrParser.hashKeyInfo(ocrJson);
        } catch (CustomException ce) {
            log.warn("OCR 사용자 예외: {}", ce.getMessage());
            throw ce;
        } catch (Exception e) {
            log.error("OCR 해시 키 생성 중 예외 발생", e);
            throw new CustomException(OcrErrorStatus._HASH_KEY_GENERATION_FAIL);
        }

        boolean isDuplicate = reviewRepository.existsByOcrKeyHash(hashKey);

        OcrDataDto ocrData;
        try {
            ocrData = ocrParser.visitCheckingInfo(ocrJson);
        } catch (CustomException ce) {
            log.warn("OCR 사용자 예외: {}", ce.getMessage());
            throw ce;
        } catch (Exception e) {
            log.error("OCR 결과 파싱 실패", e);
            throw new CustomException(OcrErrorStatus._PARSE_FAIL);
        }

        return OcrRepDto.builder()
                .ocrKeyHash(hashKey)
                .ocrData(ocrData)
                .isDuplicate(isDuplicate)
                .build();
    }
}