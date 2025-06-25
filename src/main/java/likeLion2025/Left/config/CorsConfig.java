package likeLion2025.Left.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 클라이언트의 origin(도메인) 추가
        config.setAllowedOriginPatterns(List.of(
//                "http://localhost:3000",          // 로컬 환경 (예: React 로컬 개발 서버)
//                "https://eulji-hf.netlify.app"   // 배포된 프론트엔드 URL
        ));

        // 모든 HTTP 메서드 허용 (GET, POST, PUT 등)
        config.addAllowedMethod("*");

        // 모든 요청 헤더 허용
        config.addAllowedHeader("*");

        // 'Content-Type'을 허용하는 헤더 추가
        config.addAllowedHeader("Content-Type");

        // 특정 응답 헤더를 클라이언트로 전달하도록 설정
        config.addExposedHeader("Set-Cookie");
        config.addExposedHeader("Authorization");

        // 자격 증명(쿠키, 인증 헤더 등)을 허용
        config.setAllowCredentials(true);

        // OPTIONS 메서드를 명시적으로 허용하여 preflight 요청에 응답하도록 설정
        config.addAllowedMethod("OPTIONS");

        // CORS 설정을 모든 URL 경로에 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
