package noodlezip.ocr.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ocr.dto.visitCheckingDto;
import noodlezip.ocr.util.OcrUtil;
import noodlezip.ramen.repository.RamenReviewRepository;
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

    public Map<String, Object> analyzeAndPrepareResponse(MultipartFile file) {
        String ocrJson = ocrUtil.process(file);
        String hashKey = ocrParser.hashKeyInfo(ocrJson);
        System.out.println("ocrJson : " + ocrJson);

        visitCheckingDto checkingDto = ocrParser.visitCheckingInfo(ocrJson);
        System.out.println(checkingDto);

        Map<String, Object> result = new HashMap<>();
        result.put("ocrKeyHash", hashKey);
        result.put("ocrData", checkingDto);

        return result;
    }
}