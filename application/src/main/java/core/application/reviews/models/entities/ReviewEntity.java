package core.application.reviews.models.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * {@code  ReviewRepository} 와 관련된 엔티티
 *
 * @see core.application.reviews.repositories.ReviewRepository
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ReviewEntity {
	private Long reviewId;
	private String title;
	private String content;
	private UUID userId;
	private String movieId;
	private int like;
	private Instant createdAt;
	private Instant updatedAt;

	private Set<UUID> likeUsers = new HashSet<>();

	public ReviewEntity increaseLikes(UUID userId) {
		this.like++;
		likeUsers.add(userId);
		return this;
	}

	public ReviewEntity decreaseLikes(UUID userId) {
		this.like--;
		likeUsers.remove(userId);
		return this;
	}
}
