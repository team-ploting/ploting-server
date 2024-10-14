package ploting_server.ploting.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.post.entity.Post;

import java.util.List;

/**
 * 게시글 도메인의 Repository 입니다.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMemberId(Long memberId);
}
