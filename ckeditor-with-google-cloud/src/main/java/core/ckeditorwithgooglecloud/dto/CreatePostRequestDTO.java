package core.ckeditorwithgooglecloud.dto;

import core.ckeditorwithgooglecloud.repositories.PostingContentEntity;
import lombok.Data;

@Data
public class CreatePostRequestDTO {

    private String title;
    private String content;

    public PostingContentEntity toEntity() {
        PostingContentEntity postingContentEntity = new PostingContentEntity();
        postingContentEntity.setTitle(title);
        postingContentEntity.setContent(content);
        return postingContentEntity;
    }
}
