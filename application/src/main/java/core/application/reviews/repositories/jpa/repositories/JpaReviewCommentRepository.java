package core.application.reviews.repositories.jpa.repositories;

import core.application.reviews.models.entities.*;
import org.springframework.data.jpa.repository.*;

public interface JpaReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long> {

}
