package core.application.reviews.models.dto.request.reviews;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReviewReqDTO {

    @NotNull(message = "제목이 주어지지 않았습니다.")
    @NotEmpty(message = "제목이 비어있습니다.")
    @Size(min = 1, max = 50, message = "제목은 최대 50 글자까지 가능합니다.")
    private String title;

    @NotNull(message = "본문이 주어지지 않았습니다.")
    @NotEmpty(message = "본문이 비어있습니다.")
    private String content;
}
