package core.ckeditorwithgooglecloud.controllers;

import core.ckeditorwithgooglecloud.dto.CreatePostRequestDTO;
import core.ckeditorwithgooglecloud.repositories.PostingContentEntity;
import core.ckeditorwithgooglecloud.services.ContentService;
import core.ckeditorwithgooglecloud.services.GCSService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SaveController {

    private final ContentService contentService;
    private final GCSService gcsService;

    @PostMapping("/save")
    public String saveContent(CreatePostRequestDTO postingDTO) {
        log.info(postingDTO.getTitle());
        log.info(postingDTO.getContent());

        PostingContentEntity saveResult = contentService.saveContent(postingDTO);

        log.info(saveResult.toString());

        return "redirect:/content?id=" + saveResult.getPostingId();
    }

    @PostMapping("/image/upload")       // CkEditor - main.js 에서 설정한 이미지 업로드 url
    @ResponseBody
    // CkEditor 에 이미지 첨부되면 MultipartHttpServletRequest 형태로 감싸서 뭔가 주는 듯.
    // (일부 레퍼런스에서는 MultipartFile 로 해도 되는데 난 안되서 이걸로 함)
    // CkEditor5 공식 문서에서 관련 정보를 찾을라 했는데 못찾음..
    // 여러 레퍼런스 찾다가 이렇게 하는걸 보고 따라함
    public Map<String, Object> uploadImage(MultipartHttpServletRequest request) throws IOException {
        log.info("Attempting to upload image file");

        // 요청 속 이미지 데이터 가져옴.
        // 뭐 upload 라는 속성에 실 데이터 담나봄... (관련 공식 문서 못찾음)
        MultipartFile file = request.getFile("upload");

        // 아래처럼 파일 사이즈 확인해서 용량 제한할 수도 있을 듯
        assert file != null;
        file.getSize();

        // GCS 로 이미지 저장하고 저장된 url 받아옴
        String imageUrl = null;
        try {
            imageUrl = gcsService.uploadImage(file);
            log.info("Image uploaded successfully");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // CkEditor5 문서 따라 응답 형식 맞춰줌.
        Map<String, Object> result = new HashMap<>();
        result.put("url", imageUrl);

        log.info(result.toString());

        // CkEditor 로 응답
        return result;
    }
}
