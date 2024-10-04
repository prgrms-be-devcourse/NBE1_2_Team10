package core.application.reviews.models.dto.response;

import core.application.reviews.models.entities.ReviewCommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema
public class ShowCommentsRespDTO {

    @Schema(name = "commentList", description = "조회된 댓글 내용")
    private final List<ReviewCommentEntity> commentList;

    public ShowCommentsRespDTO() {
        commentList = new ArrayList<>();
    }

    public ShowCommentsRespDTO addComments(List<ReviewCommentEntity> comments) {
        commentList.addAll(comments);
        return this;
    }
}
