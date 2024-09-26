package core.application.repositories;


import core.application.models.entities.CommentEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * 특정 영화에 달린 한줄평 댓글들을 검색
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link CommentEntity}{@code >}
     * @see #findByMovieIdOnDateDescend(String)
     * @see #findByMovieIdOnLikeDescend(String)
     * @see #findByMovieIdOnDislikeDescend(String)
     */
    List<CommentEntity> findByMovieId(String movieId);

    /**
     * 특정 영화에 달린 한줄평 댓글을 최신순으로 검색
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link CommentEntity}{@code >}
     */
    List<CommentEntity> findByMovieIdOnDateDescend(String movieId);

    /**
     * 특정 영화에 달린 한줄평 댓글을 좋아요 순으로 검색
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link CommentEntity}{@code >}
     */
    List<CommentEntity> findByMovieIdOnLikeDescend(String movieId);

    /**
     * 특정 영화에 달린 한줄평 댓글을 싫어요 순으로 검색
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link CommentEntity}{@code >}
     */
    List<CommentEntity> findByMovieIdOnDislikeDescend(String movieId);

    /**
     * DB 의 모든 한줄평 댓글을 검색
     *
     * @return {@link List}{@code <}{@link CommentEntity}{@code >}
     */
    List<CommentEntity> selectAll();
    //</editor-fold>


    // DELETE

    /**
     * 특정 한줄평 댓글을 삭제
     *
     * @param commentId 삭제할 한줄평 댓글의 ID
     */
    void deleteComment(Long commentId);
}
