package core.application.reviews.models.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommonCommentRespDTO {

    private Long reviewCommentId;
    private Long groupId;
    private Long commentRef;
    private UUID userId;
    private String content;
    private int likeCount;
    private Instant createdAt;
    private boolean isUpdated;
}
