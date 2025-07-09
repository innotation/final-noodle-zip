package noodlezip.ocr.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
public class OcrUtil {


    @Value("${naver.clova-ocr.url}")
    private String OCR_URL;
    @Value("${naver.clova-ocr.secretkey}")
    private String OCR_SECRET_KEY;

    public String process(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return sendToNaverOcr(is, file.getOriginalFilename());
        } catch (Exception e) {
            throw new RuntimeException("OCR 처리 실패", e);
        }
    }

    private String sendToNaverOcr(InputStream fileStream, String filename) {
        try {
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            URL url = new URL(OCR_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 헤더
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(30000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("X-OCR-SECRET", OCR_SECRET_KEY);

            // JSON 요청 메시지
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("version", "V2");
            jsonMap.put("requestId", UUID.randomUUID().toString());
            jsonMap.put("timestamp", System.currentTimeMillis());

            Map<String, Object> image = new HashMap<>();
            image.put("format", "jpg");
            image.put("name", filename);

            jsonMap.put("images", Collections.singletonList(image));
            String jsonMessage = mapper.writeValueAsString(jsonMap);

            // 멀티파트 바디 작성
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                // 1) JSON 메시지
                wr.writeBytes("--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"message\"\r\n\r\n");
                wr.writeBytes(jsonMessage + "\r\n");

                // 2) 이미지 파일
                wr.writeBytes("--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\r\n");
                wr.writeBytes("Content-Type: application/octet-stream\r\n\r\n");

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fileStream.read(buffer)) != -1) {
                    wr.write(buffer, 0, bytesRead);
                }
                wr.writeBytes("\r\n");

                // 3) 종료
                wr.writeBytes("--" + boundary + "--\r\n");
                wr.flush();
            }

            // 응답 처리
            int responseCode = connection.getResponseCode();
            InputStream responseStream = responseCode == 200
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(responseStream))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }



        } catch (Exception e) {
            throw new RuntimeException("OCR 서버 통신 실패", e);
        }
    }

}
