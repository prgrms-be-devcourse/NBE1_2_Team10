package core.application.movies.repositories.comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import core.application.movies.models.dto.response.CommentRespDTO;
import core.application.movies.models.entities.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * {@code COMMENT_TABLE} 과 관련된 {@code Repository}
 */
public interface CommentRepository {

	// CREATE

	/**
	 * 특정 영화에 주어진 유저 ID 로 새로운 한줄평 댓글을 DB 에 등록
	 *
	 * @param movieId 한줄평 댓글을 등록할 영화 ID
	 * @param userId  댓글을 등록하는 유저 ID
	 * @param comment 새로운 한줄평 댓글
	 * @return {@link CommentEntity} 등록된 정보
	 */
	CommentEntity saveNewComment(String movieId, UUID userId, CommentEntity comment);

	//<editor-fold desc="READ">

	/**
	 * 한줄평 댓글 ID 로 검색
	 *
	 * @param commentId 댓글 ID
	 * @return {@link Optional}{@code <}{@link CommentEntity}{@code >}
	 */
	Optional<CommentEntity> findByCommentId(Long commentId);

	/**
	 *
	 * @param movieId 영화 ID
	 * @param userId 유저 ID
	 * @return 사용자가 해당 영화에 한줄평을 작성한 기록이 있는지 확인
	 */
	Boolean existsByMovieIdAndUserId(String movieId, UUID userId);

	/**
	 * 특정 영화에 달린 한줄평 댓글들을 검색
	 *
	 * @param movieId 검색할 영화 ID
	 * @return {@link List}{@code <}{@link CommentEntity}{@code >}
	 * @see #findByMovieIdOnDateDescend(String, UUID, int)
	 * @see #findByMovieIdOnLikeDescend(String, UUID, int)
	 * @see #findByMovieIdOnDislikeDescend(String, UUID, int)
	 */
	List<CommentRespDTO> findByMovieId(String movieId, UUID userId, int offset);

	/**
	 * Mybatis 리포지토리 구현체가 특정 영화에 달린 한줄평 댓글을 최신순으로 검색
	 *
	 * @param movieId 검색할 영화 ID
	 * @return {@link List}{@code <}{@link CommentEntity}{@code >}
	 */
	List<CommentRespDTO> findByMovieIdOnDateDescend(String movieId, UUID userId, int offset);

	/**
	 * Mybatis 리포지토리 구현체가 특정 영화에 달린 한줄평 댓글을 좋아요 순으로 검색
	 *
	 * @param movieId 검색할 영화 ID
	 * @return {@link List}{@code <}{@link CommentEntity}{@code >}
	 */
	List<CommentRespDTO> findByMovieIdOnLikeDescend(String movieId, UUID userId, int offset);

	/**
	 * Mybatis 리포지토리 구현체가 특정 영화에 달린 한줄평 댓글을 싫어요 순으로 검색
	 *
	 * @param movieId 검색할 영화 ID
	 * @return {@link List}{@code <}{@link CommentEntity}{@code >}
	 */
	List<CommentRespDTO> findByMovieIdOnDislikeDescend(String movieId, UUID userId, int offset);


	/**
	 * JPA 리포지토리 구현체가 영화에 대한 한줄평 검색
	 *
	 * @param movieId 영화 ID
	 * @param userId 유저 ID
	 * @param pageable 페이지, 정렬 조건
	 * @return 정렬된 해당 페이지의 한줄평
	 */
	Page<CommentRespDTO> findByMovieIdOrderBy(String movieId,  UUID userId, Pageable pageable);

	/**
	 * DB 의 모든 한줄평 댓글을 검색
	 *
	 * @return {@link List}{@code <}{@link CommentEntity}{@code >}
	 */
	List<CommentEntity> selectAll();
	//</editor-fold>

	/**
	 * 해당 영화의 한줄평 개수 반환
	 * @param movieId 영화 ID
	 * @return 해당 영화의 한줄평 총 개수
	 */
	long countByMovieId(String movieId);

	// UPDATE

	/**
	 * 한줄평 내용 수정
	 * @param comment 수정할 한줄평
	 */
	public void update(CommentEntity comment);

	// DELETE

	/**
	 * 특정 한줄평 댓글을 삭제
	 *
	 * @param commentId 삭제할 한줄평 댓글의 ID
	 */
	void deleteComment(Long commentId);
}
