package core.application.reviews.models.dto.response.comments;

import core.application.reviews.models.entities.*;
import io.swagger.v3.oas.annotations.media.*;
import java.util.*;
import lombok.*;

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
