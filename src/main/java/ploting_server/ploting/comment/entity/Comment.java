package ploting_server.ploting.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.post.entity.Post;

import java.time.LocalDateTime;

/**
 * 댓글과 대댓글을 관리하는 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "like_count")
    private int likeCount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 댓글 삭제 (soft delete)
    public void softDeleteComment() {
        this.content = "삭제된 댓글입니다.";
    }
}
