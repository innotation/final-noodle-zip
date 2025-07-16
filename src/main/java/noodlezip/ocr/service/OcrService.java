package noodlezip.ocr.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ocr.dto.OcrDataDto;
import noodlezip.ocr.util.OcrUtil;
import noodlezip.ramen.repository.RamenReviewRepository;
import noodlezip.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OcrService {

    private final OcrUtil ocrUtil;
    private final OcrParser ocrParser;
    private final RamenReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    public Map<String, Object> analyzeAndPrepareResponse(MultipartFile file) {
        String ocrJson = ocrUtil.process(file);
        String hashKey = ocrParser.hashKeyInfo(ocrJson);
        boolean isDuplicate = reviewRepository.existsByOcrKeyHash(hashKey);

        OcrDataDto ocrData = ocrParser.visitCheckingInfo(ocrJson);

        Map<String, Object> result = new HashMap<>();
        result.put("ocrKeyHash", hashKey);
        result.put("ocrData", ocrData);
        result.put("isDuplicate", isDuplicate);

        return result;
    }
}