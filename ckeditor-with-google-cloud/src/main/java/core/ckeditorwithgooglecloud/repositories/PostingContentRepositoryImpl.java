package core.ckeditorwithgooglecloud.repositories;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostingContentRepositoryImpl implements PostingContentRepository {

    private final PostingContentMapper mapper;

    @Override
    public PostingContentEntity save(PostingContentEntity postingContentEntity) {
        mapper.save(postingContentEntity);
        return mapper.selectById(postingContentEntity.getPostingId()).orElseThrow();
    }

    @Override
    public List<PostingContentEntity> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public Optional<PostingContentEntity> selectById(Long postingId) {
        return mapper.selectById(postingId);
    }

    @Override
    public PostingContentEntity update(PostingContentEntity postingContentEntity) {
        mapper.update(postingContentEntity);
        return mapper.selectById(postingContentEntity.getPostingId()).orElseThrow();
    }

    @Override
    public void deleteById(Long postingId) {
        mapper.deleteById(postingId);
    }
}
