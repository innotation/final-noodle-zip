package noodlezip.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class WebConfig {
    /**
     * CommonsMultipartResolver 대신
     * Servlet 3.0 기반 StandardServletMultipartResolver를 사용하도록 교체하면
     * 파일 개수 제한 없이 Multipart 처리가 가능합니다.
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}