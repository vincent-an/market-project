package likeLion2025.Left.domain.post.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import likeLion2025.Left.grobal.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String localLocation = "C:\\testimage\\";
//            "/tmp/uploads/"; //추후 ubuntu 이미지 파일 경로로 변경해야됨

    //MultipartRequest request
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

    // 이미지 삭제
    public void deleteImage(String imageUrl) {
        // URL에서 파일명만 추출하여 삭제
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        s3Config.amazonS3Client().deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}
