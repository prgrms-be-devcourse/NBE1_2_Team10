package core.application.reviews.models.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommonCommentReqDTO {

    protected Long groupId;
    protected Long commentRef;

    @NotNull
    protected String content;
}
