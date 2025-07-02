package likeLion2025.Left.domain.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likeLion2025.Left.domain.user.dto.CustomUserDetails;
import likeLion2025.Left.domain.user.dto.LoginRequest;
import likeLion2025.Left.domain.user.dto.UserResponse;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.entity.enums.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/eushop/login"); // 로그인 경로를 "/eushop/login"으로 변경
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 본문에서 JSON 형식으로 데이터를 읽음
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            // JSON에서 이메일과 비밀번호 추출
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            // 이메일과 비밀번호를 콘솔에 출력하여 제대로 값이 들어왔는지 확인
            log.info("Received Email: {}", email);
            log.info("Received Password: {}", password);

            // 이메일과 비밀번호를 이용해 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
            // authenticationManager를 통해 인증을 시도하고 결과를 반환
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            // 예외 발생 시 RuntimeException을 던짐
            throw new RuntimeException("Authentication failed", e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user =customUserDetails.user();
        String email =user.getEmail();
        String role = user.getRole().getValue();

        String token = jwtUtil.createJwt(email, role);

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json; charset=UTF-8");
        UserResponse responseMessage = UserResponse.from(user, "로그인 성공!");
        new ObjectMapper().writeValue(response.getWriter(), responseMessage);
        log.info("login success email: {}", email);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        log.info("login failed");
    }
}
