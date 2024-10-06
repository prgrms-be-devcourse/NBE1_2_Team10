package core.ckeditorwithgooglecloud.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class GCSService {

    // GCS 에서 만든 우리 버킷 이름.
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${gcp.bucket.image-upload-folder}")
    private String imageStorageFolder;

    private static final String IMAGE_URL_PREFIX = "https://storage.googleapis.com";

    // GCS 에 이미지 올리기 위한 객체
    private final Storage cloudStorage;

    // 느낌상 이미지 외에도 비디오나 pdf, word 파일 같은 것도 잘 올릴 수 있을 것 같음.
    // 즉, GCS 에 저장하는 건 content type 다르다고 크게 안 다른 것 같음.
    // 그런데 이미지 말고 다른 content type upload 는 CkEditor 에서 필요한 plugin 들을 추가해야 됨.
    // 그래서 관둠.
    public String uploadImage(MultipartFile file) throws IOException {

        // GCS 에 저장될 파일 이름
        // 버킷 내 특정 폴더까지 지정 가능!
        String replacedName = imageStorageFolder + "/" + UUID.randomUUID().toString();

        // MultipartFile 컨텐츠 타입 (png, jpg, ...)
        String ext = file.getContentType();

        // 참고로 Blob 는 Binary large object 의 약어라고 함.
        // 한마디로 우리가 저장할 객체의 진짜 데이터라고 생각하면 편할듯.

        // GCS 에 저장될 객체에 대한 정보 설정
        // https://cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobInfo
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, replacedName)
                // 우리가 저장할 객체는 `buckName` 버킷에 `replacedName` 이름으로 저장될거야
                .setContentType(ext)
                // 객체의 컨텐츠 타입 지정
                .build();

        // blobInfo 와 실 데이터 (Byte) 이용해 GCS 에 새로운 객체 생성.
        // 생성 된 정보 return
        Blob blob = cloudStorage.create(blobInfo, file.getBytes());

        // GCS 에 저장된 이미지 url return
        return IMAGE_URL_PREFIX + "/" + bucketName + "/" + replacedName;
    }
}
