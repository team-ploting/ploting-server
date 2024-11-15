package ploting_server.ploting.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.post.entity.PostLike;

import java.util.List;

/**
 * 게시글 좋아요 도메인의 Repository 입니다.
 */
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findAllByPostId(Long postId);
}
