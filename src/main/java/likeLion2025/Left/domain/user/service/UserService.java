package likeLion2025.Left.domain.user.service;

import jakarta.transaction.Transactional;
import likeLion2025.Left.domain.user.dto.SignupRequest;
import likeLion2025.Left.domain.user.dto.UserResponse;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.entity.enums.Role;
import likeLion2025.Left.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserResponse joinProcess(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 이메일 도메인 검증 (이메일이 @g.eulji.ac.kr로 끝나는지 확인)
        if (!signupRequest.getEmail().endsWith("@g.eulji.ac.kr")) {
            throw new RuntimeException("제대로 된 이메일 형식을 적어주세요. (학교 이메일만 허용됩니다.)");
        }

        User newUser =User.builder()
                .email(signupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .department(signupRequest.getDepartment())
                .role(Role.fromValue("ROLE_USER"))
//                .role(Role.valueOf("ROLE_USER")) 에러로 변경
                .build();

        userRepository.save(newUser);

        return UserResponse.from(newUser, "회원가입 성공!");
    }
}
