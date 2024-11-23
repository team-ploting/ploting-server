package ploting_server.ploting.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.comment.dto.response.CommentInfoResponse;
import ploting_server.ploting.comment.repository.CommentLikeRepository;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.PostErrorCode;
import ploting_server.ploting.core.exception.MeetingException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.PostException;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.point.entity.LevelType;
import ploting_server.ploting.post.dto.request.PostCreateRequest;
import ploting_server.ploting.post.dto.request.PostUpdateRequest;
import ploting_server.ploting.post.dto.response.PostInfoResponse;
import ploting_server.ploting.post.dto.response.PostListResponse;
import ploting_server.ploting.post.entity.Post;
import ploting_server.ploting.post.entity.PostLike;
import ploting_server.ploting.post.repository.PostLikeRepository;
import ploting_server.ploting.post.repository.PostRepository;

import java.util.List;

/**
 * 게시글을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;

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
                .commentCount(0)
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
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND_POST_ID));

        // 게시글의 작성자인지 확인
        checkPostAuthor(memberId, post);

        post.updatePost(postUpdateRequest);
    }

    /**
     * 게시글을 삭제합니다.
     */
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MeetingException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND_POST_ID));

        // 게시글의 작성자인지 확인
        checkPostAuthor(memberId, post);

        List<PostLike> postLikes = postLikeRepository.findAllByPostId(postId);

        // 게시글의 좋아요 삭제
        postLikeRepository.deleteAll(postLikes);

        // 양방향 연관관계 해제
        member.removePost(post);

        // 게시글 삭제
        postRepository.delete(post);
    }

    /**
     * 게시글의 세부 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public PostInfoResponse getPostInfo(Long memberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND_POST_ID));

        List<CommentInfoResponse> commentInfoResponse = post.getComments().stream()
                .map(comment -> CommentInfoResponse.builder()
                        .authorNickname(comment.getMember().getNickname())
                        .authorLocation(comment.getMember().getLocation())
                        .authorLevel(comment.getMember().getLevel())
                        .authorLevelType(LevelType.findLevelTypeByLevel(comment.getMember().getLevel()))
                        .content(comment.getContent())
                        .likeCount(comment.getLikeCount())
                        .hasLiked(commentLikeRepository.existsByMemberIdAndCommentId(memberId, comment.getId()))
                        .myComment(comment.getMember().getId().equals(memberId))
                        .build())
                .toList();

        return PostInfoResponse.builder()
                .authorNickname(post.getMember().getNickname())
                .authorLocation(post.getMember().getLocation())
                .authorLevel(post.getMember().getLevel())
                .authorLevelType(LevelType.findLevelTypeByLevel(post.getMember().getLevel()))
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .hasLiked(postLikeRepository.existsByMemberIdAndPostId(memberId, postId))
                .myPost(post.getMember().getId().equals(memberId))
                .comments(commentInfoResponse)
                .build();
    }

    /**
     * 내가 작성한 게시글을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<PostListResponse> getMyPosts(Long memberId) {
        List<Post> myPosts = postRepository.findAllByMemberId(memberId);

        return myPosts.stream()
                .map(post -> PostListResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .authorNickName(post.getMember().getNickname())
                        .likeCount(post.getLikeCount())
                        .commentCount(post.getCommentCount())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList();
    }

    /**
     * 내가 작성한 댓글의 게시글을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<PostListResponse> getPostsFromMyComments(Long memberId) {
        List<Post> postsByMyComments = postRepository.findAllByCommentedMemberId(memberId);

        return postsByMyComments.stream()
                .map(post -> PostListResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .authorNickName(post.getMember().getNickname())
                        .likeCount(post.getLikeCount())
                        .commentCount(post.getCommentCount())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList();
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
