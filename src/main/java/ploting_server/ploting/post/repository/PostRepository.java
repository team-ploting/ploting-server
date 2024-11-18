package ploting_server.ploting.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ploting_server.ploting.post.entity.Post;

import java.util.List;

/**
 * 게시글 도메인의 Repository 입니다.
 */
public interface
PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE :search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Post> searchPosts(@Param("search") String search);
}
