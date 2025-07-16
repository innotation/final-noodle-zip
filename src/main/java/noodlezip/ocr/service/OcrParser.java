package noodlezip.ocr.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import noodlezip.ocr.dto.OcrDataDto;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OcrParser {

    private final ObjectMapper mapper = new ObjectMapper();

    /* ===============  공용 유틸  =============== */
    private Optional<String> tryGetText(JsonNode root, String jsonPointer) {
        JsonNode n = root.at(jsonPointer);
        if (n.isMissingNode() || n.asText().isBlank()) return Optional.empty();
        return Optional.of(n.asText());
    }


    public String hash(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();  // 64자리 고정된 해시 문자열
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 해시 생성 실패", e);
        }
    }

    /* ===============  Hash용 정보  =============== */
    public String hashKeyInfo(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode result = root.at("/images/0/receipt/result");
            String raw = "";
            if (result.isArray()) {
                throw new Exception("OCR 결과로 고유 키를 생성할 수 없습니다.");
            }else {
                String bizNum = tryGetText(result, "/storeInfo/bizNum/text").orElse("").replaceAll("-", "");
                String confirmNum = tryGetText(result, "/paymentInfo/confirmNum/text").orElse("").replaceAll("-", "");
                String dateTime = tryGetText(result, "/paymentInfo/date/text").orElse("") + " " + tryGetText(result, "/paymentInfo/time/text").orElse("");
                String storeName = tryGetText(result, "/storeInfo/name/text").orElse("").replaceAll("[^가-힣a-zA-Z0-9\\s]", "");
                String totalPrice = tryGetText(result, "/totalPrice/price/text").orElse("/subResult/price/text");

                if (!bizNum.isEmpty() && !confirmNum.isEmpty()) {
                    raw = bizNum + "|" + confirmNum;
                } else if (!bizNum.isEmpty() && !dateTime.isBlank()) {
                    raw = bizNum + "|" + dateTime;
                } else if (!storeName.isEmpty() && !totalPrice.isBlank() && !dateTime.isBlank()) {
                    raw = storeName + "|" + totalPrice + "|" + dateTime;
                }
                return hash(raw);
            }
        } catch (Exception e) {
            throw new RuntimeException("OCR 키 정보 추출 실패.",e);
        }
    }

    // 사용자에게 방문확인 받기 위한 정보
    public OcrDataDto visitCheckingInfo(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode result = root.at("/images/0/receipt/result");

            String storeName = tryGetText(result, "/storeInfo/name/text").orElse("").replaceAll("[^가-힣a-zA-Z0-9\\s]", "");
            String date = tryGetText(result, "/paymentInfo/date/text").orElse("");
            String bizNum = tryGetText(result, "/storeInfo/bizNum/text").orElse("").replaceAll("-", "");

            List<String> menuNames = new ArrayList<>();
            JsonNode itemNodes = result.at("/subResults/0/items");

            if (itemNodes.isArray()) {
                for (JsonNode node : itemNodes) {
                    String name = node.at("/name/text").asText("");
                    if (!name.isEmpty()) {
                        menuNames.add(name);
                    }
                }
            }
            return new OcrDataDto(storeName, date, menuNames, bizNum);
        } catch (Exception e) {
            throw new RuntimeException("OCR 전체 정보 추출 실패", e);
        }
    }

}
