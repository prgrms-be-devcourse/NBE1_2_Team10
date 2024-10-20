package core.application.reviews.repositories.jpa.repositories;

import core.application.reviews.models.entities.*;
import org.springframework.data.jpa.repository.*;

public interface JpaReviewRepository extends JpaRepository<ReviewEntity, Long> {

}
