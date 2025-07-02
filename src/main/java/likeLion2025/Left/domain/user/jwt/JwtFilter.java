package likeLion2025.Left.domain.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likeLion2025.Left.domain.user.dto.CustomUserDetails;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 1. 필터 제외할 경로 지정 (로그인, 회원가입 등)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Objects.equals("/eushop/login", path) ||
                Objects.equals("/eushop/signup", path) ||
                Objects.equals("/eushop", path); // 일단 메인페이지도 필터 제외
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        // authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("null token");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        // 토큰 만료 시 401 응답 후 종료
        if (jwtUtil.isExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token expired");
            return;
        }

        //토큰에서 username 획득
        String email = jwtUtil.getEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //CustomUserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        //스프링 시큐리티 인증 어스토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //어스토큰을 가지고 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        //다음 필터로 값 넘겨줌
        filterChain.doFilter(request, response);

    }
}
