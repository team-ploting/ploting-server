package ploting_server.ploting.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.comment.dto.request.CommentCreateRequest;
import ploting_server.ploting.comment.entity.Comment;
import ploting_server.ploting.comment.repository.CommentLikeRepository;
import ploting_server.ploting.comment.repository.CommentRepository;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.PostErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.PostException;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.post.entity.Post;
import ploting_server.ploting.post.repository.PostRepository;

/**
 * 댓글을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글을 생성합니다.
     */
    @Transactional
    public void createComment(Long memberId, Long postId, CommentCreateRequest commentCreateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND_POST_ID));

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(commentCreateRequest.getContent())
                .likeCount(0)
                .build();

        commentRepository.save(comment);

        // 양방향 연관관계 설정
        member.addComment(comment);

        // 양방향 연관관계 설정
        post.addComment(comment);
    }
}
