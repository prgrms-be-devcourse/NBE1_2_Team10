package core.application.reviews.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GCSService {

    private final String bucketName = "your-gcs-bucket-name"; // GCS 버킷 이름

    // Google Cloud Storage 클라이언트
    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        storage.create(blobInfo, file.getInputStream());

        // 버킷에 파일 업로드 & URL 반환
        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }
}
