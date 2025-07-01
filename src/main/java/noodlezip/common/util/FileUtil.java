package noodlezip.common.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUtil {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.endpoint}")
    private String endPoint;

    public Map<String, String> fileupload(String folderName, MultipartFile file) {
        if (file.isEmpty()) {
            // 나중에 예외처리해야됨
            log.warn("업로드 파일이 비어 있습니다. 폴더: {}", folderName);
            return new HashMap<>(); // 빈 Map 반환
        }

        String s3FolderPath = folderName + DateTimeFormatter.ofPattern("/yyyyMMdd").format(LocalDate.now()) + "/";


        createS3Folder(bucket, s3FolderPath);

        String originalFilename = file.getOriginalFilename(); // 원본 파일명
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filesystemName = UUID.randomUUID().toString().replace("-", "") + fileExtension; // 고유한 시스템 파일명

        // S3에 최종적으로 저장될 객체 키 (경로 포함)
        // 예: "profile/20250630/uuid-filename.jpg"
        String s3ObjectKey = s3FolderPath + filesystemName;

        // 파일 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType()); // 파일의 MIME 타입 설정

        try (InputStream inputStream = file.getInputStream()) {
            // S3에 파일 업로드 요청
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, s3ObjectKey, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            s3Client.putObject(putObjectRequest); // 파일 업로드 실행

            // 업로드된 파일의 S3 URL 생성
            String fileUrl = s3Client.getUrl(bucket, s3ObjectKey).toString();

            Map<String, String> result = new HashMap<>();
            result.put("filePath", s3FolderPath); // 버킷 내 폴더 경로
            result.put("originalFilename", originalFilename);
            result.put("filesystemName", filesystemName);
            result.put("fileUrl", fileUrl); // S3에서 접근 가능한 전체 URL, 이 값으로 이미지 출력
            log.info("파일 업로드 성공: {}", fileUrl);
            return result;

        } catch (AmazonS3Exception e) {
            log.error("S3 파일 업로드 중 AmazonS3Exception 발생: {}", s3ObjectKey, e);
            throw new RuntimeException("S3 파일 업로드에 실패했습니다. (S3 Exception)", e);
        } catch (SdkClientException e) {
            log.error("S3 파일 업로드 중 SdkClientException 발생: {}", s3ObjectKey, e);
            throw new RuntimeException("S3 파일 업로드에 실패했습니다. (SDK Client Exception)", e);
        } catch (IOException e) {
            log.error("파일 스트림 처리 중 IOException 발생: {}", s3ObjectKey, e);
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void createS3Folder(String bucketName, String folderKey) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0L); // 폴더 객체는 내용이 없음
        metadata.setContentType("application/x-directory"); // MIME 타입 지정 (콘솔에서 폴더로 인식)

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderKey,
                new ByteArrayInputStream(new byte[0]), metadata);

        try {
            s3Client.putObject(putObjectRequest);
            log.info("S3 폴더 생성 또는 업데이트: {}", folderKey);
        } catch (AmazonS3Exception e) {
            log.error("S3 폴더 생성 중 AmazonS3Exception 발생: {}", folderKey, e);
            // 폴더 생성 실패 시에도 파일 업로드를 계속할지 여부는 정책에 따라 결정
            // 여기서는 파일 업로드를 막기 위해 RuntimeException을 던질 수도 있습니다.
        } catch (SdkClientException e) {
            log.error("S3 폴더 생성 중 SdkClientException 발생: {}", folderKey, e);
        }
    }

    public void deleteFileFromS3(String fileUrl) {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            log.warn("삭제할 S3 파일 URL이 null이거나 비어 있습니다.");
            return;
        }

        try {

            // 전체 url에서 이미지 값만 추출
            String s3ObjectKey;
            String urlPrefix = endPoint + "/" + bucket + "/";
            if (fileUrl.startsWith(urlPrefix)) {
                s3ObjectKey = fileUrl.substring(urlPrefix.length());
            } else {
                // 만약 URL이 예상과 다르면 전체를 키로 간주하거나 오류 처리
                log.warn("S3 URL 형식이 예상과 다릅니다. URL: {}", fileUrl);
                s3ObjectKey = fileUrl;
            }

            s3Client.deleteObject(bucket, s3ObjectKey);
            log.info("S3 파일 삭제 성공: {}", s3ObjectKey);
        } catch (AmazonS3Exception e) {
            log.error("S3 파일 삭제 중 AmazonS3Exception 발생: {}. 메시지: {}", fileUrl, e.getErrorMessage(), e);
        } catch (SdkClientException e) {
            log.error("S3 파일 삭제 중 SdkClientException 발생: {}. 메시지: {}", fileUrl, e.getMessage(), e);
        } catch (Exception e) {
            log.error("S3 파일 삭제 중 알 수 없는 오류 발생: {}. 메시지: {}", fileUrl, e.getMessage(), e);
        }
    }

}
