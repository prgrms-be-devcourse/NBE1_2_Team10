package core.application.reviews.models.dto.response;

import core.application.reviews.models.entities.ReviewCommentEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShowCommentsRespDTO {

    private final List<ReviewCommentEntity> commentList;

    public ShowCommentsRespDTO() {
        commentList = new ArrayList<>();
    }

    public ShowCommentsRespDTO addComments(List<ReviewCommentEntity> comments) {
        commentList.addAll(comments);
        return this;
    }
}
