package core.ckeditorwithgooglecloud.repositories;

import java.util.List;
import java.util.Optional;


public interface PostingContentRepository {

    PostingContentEntity save(PostingContentEntity postingContentEntity);

    List<PostingContentEntity> selectAll();

    Optional<PostingContentEntity> selectById(Long postingId);

    PostingContentEntity update(PostingContentEntity postingContentEntity);

    void deleteById(Long postingId);
}
