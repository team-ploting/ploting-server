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
import ploting_server.ploting.post.dto.request.PostCreateRequest;
import ploting_server.ploting.post.dto.request.PostUpdateRequest;
import ploting_server.ploting.post.entity.Post;
import ploting_server.ploting.post.repository.PostRepository;

/**
 * 게시글을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /**
     * 게시글을 생성합니다.
     */
    @Transactional
    public void createPost(Long memberId, PostCreateRequest postCreateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Post post = Post.builder()
                .member(member)
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .likeCount(0)
                .build();

        postRepository.save(post);

        // 양방향 연관관계 설정
        member.addPost(post);
    }

    /**
     * 게시글을 수정합니다.
     */
    @Transactional
    public void updatePost(Long memberId, Long postId, PostUpdateRequest postUpdateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND_POST_ID));

        // 게시글의 작성자인지 확인
        checkPostAuthor(memberId, post);

        post.updatePost(postUpdateRequest);
    }

    /**
     * 게시글의 작성자인지 확인합니다.
     */
    private void checkPostAuthor(Long memberId, Post post) {
        if (!post.getMember().getId().equals(memberId)) {
            throw new PostException(PostErrorCode.NOT_POST_AUTHOR);
        }
    }
}
