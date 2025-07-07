package likeLion2025.Left.grobal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        //controller에서 cors문제 처리
        corsRegistry.addMapping("/**")  // 모든 경로에 대해 CORS 설정
                .allowedOriginPatterns("http://localhost:3000", "http://127.0.0.1:3000", "https://leftlion.netlify.app");

    }
}
