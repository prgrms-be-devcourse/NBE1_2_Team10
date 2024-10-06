package core.application.reviews.models.dto.response;

import core.application.reviews.models.entities.ReviewCommentEntity;

public class CreateCommentRespDTO extends CommonCommentRespDTO {

    public static CreateCommentRespDTO toDTO(ReviewCommentEntity entity) {
        return (CreateCommentRespDTO) new CreateCommentRespDTO()
                .setReviewCommentId(entity.getReviewCommentId())
                .setGroupId(entity.getGroupId())
                .setCommentRef(entity.getCommentRef())
                .setUserId(entity.getUserId())
                .setContent(entity.getContent())
                .setLikeCount(entity.getLike())
                .setCreatedAt(entity.getCreatedAt())
                .setUpdated(entity.isUpdated());
    }
}
