package core.application.reviews.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommonCommentReqDTO {

    protected Long groupId;
    protected Long commentRef;

    @NotNull(message = "댓글 내용이 존재하지 않습니다.")
    @NotEmpty(message = "Content can not be empty")
    protected String content;
}
