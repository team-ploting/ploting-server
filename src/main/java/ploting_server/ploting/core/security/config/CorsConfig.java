package ploting_server.ploting.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS 설정 클래스입니다.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 Origin(출처)
        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:8080",
                        "http://127.0.0.1:8080",
                        "http://localhost:5173",
                        "http://127.0.0.1:5173",
                        "https://ploting.kr",
                        "https://api.ploting.kr"
                )
        );

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // 허용할 헤더
        configuration.setAllowedHeaders(
                Arrays.asList(
                        "*"
                )
        );

        // 인증 정보(쿠키, Authorization 헤더 등)를 포함한 요청 허용
        configuration.setAllowCredentials(true);

        // 모든 경로에 대해 CORS 설정 적용
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
