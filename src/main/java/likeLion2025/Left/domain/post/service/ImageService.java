package likeLion2025.Left.domain.post.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import likeLion2025.Left.domain.post.entity.Post;
import likeLion2025.Left.domain.post.repository.PostRepository;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import likeLion2025.Left.grobal.config.S3Config;
import likeLion2025.Left.grobal.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final S3Config s3Config;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String localLocation = "C:\\testimage\\";
//            "/tmp/uploads/"; //추후 ubuntu 이미지 파일 경로로 변경해야됨

    // 이미지 추가
    public String imageUpload(MultipartFile file) throws IOException {
        // 프론트에서 이미지를 file이라는 이름으로 보냈을 때 그 갑을 받음
//        MultipartFile file = request.getFile("upload");

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        // 이미지 파일 이름 : 고유의 랜덤 UUID를 부여하여 이미지 겹침 방지
        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName; // 로컬(ubuntu)에 저장될 위치 지정

        File localFile = new File(localPath); // 뽑아낸 이미지를 localFile이라는 공간에 저장
        file.transferTo(localFile); // localFile을 로컬(ubuntu)에 저장

        // putObject의 new PutObjectRequest 객체를 통해 이미지를 S3 버킷으로 이동
        // withCannedAcl는 외부에서 올린 파일을 public으로 읽을 수 있게 하는 보안설정
        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        // S3에 올린 이미지 주소 받아오기 (버킷 이름과 이미지 이름으로 조회하여 String형으로 주소를 받아옴)
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        //서버에 저장한 이미지 삭제
        localFile.delete();

        // 컨트롤러로 이미지 s3url 전송
        return s3Url;
    }

    // 다중 이미지 삭제
    public void deleteImages(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            s3Config.amazonS3Client().deleteObject(new DeleteObjectRequest(bucket, fileName));
        }
    }

    // 단일 이미지 삭제
    public void deleteImage(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        s3Config.amazonS3Client().deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    // 이미지 수정 (잘 안됨.. ㅠ)
//    public String replaceImage(String oldImageUrl, MultipartFile newImage) throws IOException {
//        // 기존 이미지 삭제
//        deleteImage(oldImageUrl);
//
//        // 새 이미지 업로드 (기존 메서드 재사용)
//        return imageUpload(newImage);
//    }
    // 이미지 수정
//    @Transactional
//    public void updatePostImage(Long postId, String oldImageUrl, MultipartFile newImage, String userEmail) throws IOException {
//        // 게시글 검증
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//        // 작성자 검증
//        if (!post.getUser().getEmail().equals(userEmail)) {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }
//
//        // 이미지 교체
//        String newImageUrl = replaceImage(oldImageUrl, newImage);
//
//        // 이미지 리스트에서 교체
//        List<String> updatedUrls = post.getImgUrls().stream()
//                .map(url -> url.equals(oldImageUrl) ? newImageUrl : url)
//                .collect(Collectors.toList());
//
//        post.setImgUrls(updatedUrls);
//
//        // 대표 이미지도 교체된 이미지가 첫 번째라면 업데이트
//        if (post.getIntroImgUrl().equals(oldImageUrl)) {
//            post.setIntroImgUrl(newImageUrl);
//        }
//    }

}
