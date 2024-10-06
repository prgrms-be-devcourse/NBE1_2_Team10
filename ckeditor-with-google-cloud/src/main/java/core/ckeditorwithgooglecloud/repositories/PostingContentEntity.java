package core.ckeditorwithgooglecloud.repositories;

import lombok.Data;

@Data
public class PostingContentEntity {

    private Long postingId;
    private String title;
    private String content;
}
