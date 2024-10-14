package ploting_server.ploting.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.comment.entity.Comment;

import java.util.List;

/**
 * 댓글 도메인의 Repository 입니다.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMemberId(Long memberId);
}
