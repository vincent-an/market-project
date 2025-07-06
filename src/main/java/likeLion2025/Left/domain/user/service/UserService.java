package likeLion2025.Left.domain.user.service;

import jakarta.transaction.Transactional;
import likeLion2025.Left.domain.post.dto.response.PostMainIntroResponse;
import likeLion2025.Left.domain.post.entity.Post;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.post.repository.PostRepository;
import likeLion2025.Left.domain.user.dto.MyPostsDTO;
import likeLion2025.Left.domain.user.dto.request.SignupRequest;
import likeLion2025.Left.domain.user.dto.UserProfileDTO;
import likeLion2025.Left.domain.user.dto.request.UserUpdateRequest;
import likeLion2025.Left.domain.user.dto.response.BookmarkResponse;
import likeLion2025.Left.domain.user.dto.response.UserResponse;
import likeLion2025.Left.domain.user.dto.response.UserUpdateResponse;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.entity.enums.Role;
import likeLion2025.Left.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
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

    // 프로필 조회
    public UserProfileDTO getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(("해당 유저를 찾을 수 없습니다.")));

        return new UserProfileDTO(user.getEmail(), user.getNickname(), user.getDepartment());
    }

    //개인정보 수정
    public UserUpdateResponse updateProfile(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        user.setNickname(request.getNickname());
        user.setDepartment(request.getDepartment());
        log.info("변경된 닉네임: {}", request.getNickname());
        log.info("변경된 학과: {}", request.getDepartment());
        return UserUpdateResponse.from(user, "정보 수정 완료!");
    }

    //내가 등록한 상품
    public List<MyPostsDTO> getUserPostsByType(String email, PostType type, List<PostStatus> statuses) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByUserEmailAndPostTypeAndStatusIn(email, type, statuses);

        return posts.stream()
                .map(post -> MyPostsDTO.builder()
                        .introImgUrl(post.getIntroImgUrl())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .category(post.getCategory().getValue())
                        .status(post.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    // 상태 변경 기능
    @Transactional
    public String togglePostStatus(Long postId, Long userId) {
        // 게시글 조회 및 권한 확인
        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없거나 권한이 없습니다."));

        // 현재 상태를 확인하고 상태를 반전시킴
        PostStatus currentStatus = post.getStatus();
        PostStatus newStatus;

        switch (currentStatus) {
            case SELLING -> newStatus = PostStatus.SOLD;
            case SOLD -> newStatus = PostStatus.SELLING;
            case BUYING -> newStatus = PostStatus.BOUGHT;
            case BOUGHT -> newStatus = PostStatus.BUYING;
            default -> throw new IllegalStateException("알 수 없는 상태입니다.");
        }

        // 상태 변경
        post.setStatus(newStatus);
        log.info("상태 변경한 게시글 번호: {}", postId);
        log.info("상태 변경 완료, 새로운 상태: {}", newStatus);
        // 상태 변경 완료 메시지 반환
        return "상태 변경 완료되었습니다. 새로운 상태: " + newStatus;
    }

    //찜 버튼 동작 기능
    @Transactional
    public BookmarkResponse toggleBookmark(Long postId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (user.getLikedPosts().contains(post)) {
            user.getLikedPosts().remove(post);
            return BookmarkResponse.from(user, postId, "찜 해제 완료");
        } else {
            user.getLikedPosts().add(post);
            return BookmarkResponse.from(user, postId, "찜 추가 완료");
        }
    }

    //찜 목록 조회 (내일 테스트 필요)
    public List<PostMainIntroResponse> getLikedPosts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getLikedPosts().stream()
                .map(post -> PostMainIntroResponse.builder()
                        .introImgUrl(post.getIntroImgUrl())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .category(post.getCategory().getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
