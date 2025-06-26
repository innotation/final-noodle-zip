package noodlezip.common.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("noodle-zip")
                        .description("OCR 기반 영수증 인증을 활용한 신뢰도 중심의 라멘 전문 미식 리뷰 플랫폼 개발")
                        .version("0.0.0"));
    }
}
