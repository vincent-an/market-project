package likeLion2025.Left.domain.post.controller;

import likeLion2025.Left.domain.post.repository.PostRepository;
import likeLion2025.Left.domain.post.service.ImageService;
import likeLion2025.Left.domain.user.dto.CustomUserDetails;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/eushop/image/")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final PostRepository postRepository;


    @PostMapping("/upload")
    public Map<String, Object> uploadImages(Authentication authentication,MultipartRequest request) throws IOException {
        //사용자 인증
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email  = authentication.getName();

        List<MultipartFile> files = request.getFiles("upload");
        List<String> imageUrls = new ArrayList<>();
        String introImgUrl = null;

        for (int i = 0; i < files.size(); i++) {
//            String url = imageService.imageUpload(request);
            MultipartFile file = files.get(i);
            String url = imageService.imageUpload(file); // 수정된 메서드 사용
            imageUrls.add(url);
            if (i == 0) {
                introImgUrl = url; // 첫 번째 이미지를 대표 이미지로 저장
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("imgUrls", imageUrls);
        response.put("introImgUrl", introImgUrl); // 대표 이미지 URL 포함
        return response;
    }

    // 이미지 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(Authentication authentication, @RequestParam String imageUrl) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        try {
            imageService.deleteImage(imageUrl);
            return ResponseEntity.ok("{\"message\": \"이미지 삭제 성공\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"이미지 삭제 실패\"}");
        }
    }

    // 이미지 수정


}
