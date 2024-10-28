package ploting_server.ploting.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.comment.entity.Comment;
import ploting_server.ploting.comment.repository.CommentRepository;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.member.dto.request.MemberRegisterRequest;
import ploting_server.ploting.member.dto.request.MemberUpdateRequest;
import ploting_server.ploting.member.dto.response.MemberInfoResponse;
import ploting_server.ploting.member.dto.response.MemberNicknameDuplicationResponse;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.post.entity.Post;
import ploting_server.ploting.post.repository.PostRepository;

import java.util.List;

/**
 * 회원을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 회원 가입 시 회원 정보를 추가적으로 등록합니다.
     */
    @Transactional
    public void registerMemberInfo(Long memberId, MemberRegisterRequest memberRegisterRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        findMember.registerMember(memberRegisterRequest);
    }

    /**
     * 회원의 정보를 반환합니다.
     */
    @Transactional(readOnly = true)
    public MemberInfoResponse getMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        return MemberInfoResponse.builder()
                .profileImageUrl(findMember.getProfileImageUrl())
                .name(findMember.getName())
                .nickname(findMember.getNickname())
                .location(findMember.getLocation())
                .gender(findMember.getGender())
                .birth(findMember.getBirth())
                .level(findMember.getLevel())
                .createdAt(findMember.getCreatedAt())
                .activeStatus(findMember.isActiveStatus())
                .build();
    }

    /**
     * 회원 닉네임 중복 여부를 확인합니다.
     */
    @Transactional(readOnly = true)
    public MemberNicknameDuplicationResponse checkNicknameDuplicate(String nickname) {
        Boolean existsByNickname = memberRepository.existsByNickname(nickname);

        return MemberNicknameDuplicationResponse.builder()
                .isMemberNicknameDuplicated(existsByNickname)
                .build();
    }

    /**
     * 회원 정보를 수정합니다.
     */
    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        findMember.updateMember(memberUpdateRequest);
    }

    /**
     * 회원 탈퇴합니다. (soft delete)
     */
    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 회원 soft delete
        findMember.softDeleteMember();

        // 회원의 게시글 soft delete
        List<Post> postsByFindMember = postRepository.findByMemberId(memberId);
        for (Post post : postsByFindMember) {
            post.softDeletePost();
        }

        // 대댓글 없는 경우 댓글 삭제, 대댓글 있는 경우 soft delete
        List<Comment> commentsByFindMember = commentRepository.findByMemberId(memberId);
        for (Comment comment : commentsByFindMember) {
            if (comment.getParentComment() == null) {
                commentRepository.delete(comment);
                continue;
            }
            comment.softDeleteComment();
        }

        // TODO: 회원이 속한 단체와 모임 row 삭제, 단체와 모임 리더라면 reject
    }
}
