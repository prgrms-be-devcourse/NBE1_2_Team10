package core.ckeditorwithgooglecloud.services;

import core.ckeditorwithgooglecloud.dto.CreatePostRequestDTO;
import core.ckeditorwithgooglecloud.repositories.PostingContentEntity;
import core.ckeditorwithgooglecloud.repositories.PostingContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final PostingContentRepository postingContentRepository;

    public PostingContentEntity saveContent(CreatePostRequestDTO requestDTO) {
        PostingContentEntity newEntity = requestDTO.toEntity();
        return postingContentRepository.save(newEntity);
    }

    public PostingContentEntity getContent(Long id) {
        return postingContentRepository.selectById(id).orElseThrow();
    }
}
