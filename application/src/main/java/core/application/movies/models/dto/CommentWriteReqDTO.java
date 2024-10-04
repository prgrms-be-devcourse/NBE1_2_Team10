package core.application.movies.models.dto;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentWriteReqDTO {
	@NotBlank(message = "공백은 입력할 수 없습니다.")
	private String content;

	@Range(min = 1, max = 10, message = "1점에서 10점 이내로 평점을 입력해주세요.")
	private int rating;
}
