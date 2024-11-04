package core.application.reviews.models.dto.response.reviews;

import core.application.reviews.models.entities.ReviewEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ListReviewsRespDTO {

    private List<ReviewEntity> reviewList;

    public ListReviewsRespDTO() {
        reviewList = new ArrayList<>();
    }

    public static ListReviewsRespDTO of(List<ReviewEntity> reviewList) {
        ListReviewsRespDTO reviewsRespDTO = new ListReviewsRespDTO();
        reviewsRespDTO.reviewList.addAll(reviewList);
        return reviewsRespDTO;
    }
}
