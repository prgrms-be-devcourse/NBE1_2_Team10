package core.application.movies.repositories.comment;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.application.movies.repositories.mapper.CommentLikeMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepository {
	private final CommentLikeMapper commentLikeMapper;

	public void saveCommentLike(Long commentId, UUID userId) {
		commentLikeMapper.save(commentId, userId);
	}

	public boolean isExistLike(Long commentId, UUID userId) {
		int count = commentLikeMapper.countLikeByUser(commentId, userId);
		return count != 0;
	}

	public void deleteCommentLike(Long commentId, UUID userId) {
		commentLikeMapper.delete(commentId, userId);
	}
}
