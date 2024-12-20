package ploting_server.ploting.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.comment.entity.CommentLike;

import java.util.List;
import java.util.Optional;

/**
 * 댓글 좋아요 도메인의 Repository 입니다.
 */
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    List<CommentLike> findAllByMemberId(Long memberId);

    List<CommentLike> findAllByCommentId(Long commentId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId);
}
