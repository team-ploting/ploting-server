package ploting_server.ploting.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.comment.entity.Comment;
import ploting_server.ploting.comment.entity.CommentLike;
import ploting_server.ploting.comment.repository.CommentLikeRepository;
import ploting_server.ploting.comment.repository.CommentRepository;
import ploting_server.ploting.core.code.error.CommentErrorCode;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.CommentException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;

import java.util.Optional;

/**
 * 댓글 좋아요를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 좋아요를 누릅니다.
     */
    @Transactional
    public void pressCommentLike(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND_COMMENT_ID));

        Optional<CommentLike> checkCommentLike = commentLikeRepository.findByMemberIdAndCommentId(memberId, commentId);

        if (checkCommentLike.isPresent()) {
            throw new CommentException(CommentErrorCode.ALREADY_EXIST_COMMENT_LIKE);
        }

        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();

        // 좋아요 수 증가
        comment.incrementLikeCount();

        commentLikeRepository.save(commentLike);
    }
}
