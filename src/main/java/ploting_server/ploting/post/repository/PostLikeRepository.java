package ploting_server.ploting.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.post.entity.PostLike;

import java.util.List;
import java.util.Optional;

/**
 * 게시글 좋아요 도메인의 Repository 입니다.
 */
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findAllByMemberId(Long memberId);

    List<PostLike> findAllByPostId(Long postId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
