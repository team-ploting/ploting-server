package ploting_server.ploting.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.PostErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.PostException;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.post.entity.Post;
import ploting_server.ploting.post.entity.PostLike;
import ploting_server.ploting.post.repository.PostLikeRepository;
import ploting_server.ploting.post.repository.PostRepository;

import java.util.Optional;

/**
 * 게시글 좋아요를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 좋아요를 누릅니다.
     */
    @Transactional
    public void pressPostLike(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND_POST_ID));

        Optional<PostLike> checkPostLike = postLikeRepository.findByMemberIdAndPostId(memberId, postId);

        if (checkPostLike.isPresent()) {
            throw new PostException(PostErrorCode.ALREADY_EXIST_POST_LIKE);
        }

        PostLike postLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();

        // 좋아요 수 증가
        post.incrementLikeCount();

        postLikeRepository.save(postLike);
    }
}
