package ploting_server.ploting.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.comment.entity.CommentLike;
import ploting_server.ploting.comment.repository.CommentLikeRepository;
import ploting_server.ploting.core.code.error.MeetingErrorCode;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.OrganizationErrorCode;
import ploting_server.ploting.core.exception.MeetingException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.OrganizationException;
import ploting_server.ploting.meeting.entity.MeetingLike;
import ploting_server.ploting.meeting.entity.MeetingMember;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingMemberRepository;
import ploting_server.ploting.member.dto.request.MemberRegisterRequest;
import ploting_server.ploting.member.dto.request.MemberUpdateRequest;
import ploting_server.ploting.member.dto.response.MemberInfoResponse;
import ploting_server.ploting.member.dto.response.MemberNicknameDuplicationResponse;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.entity.OrganizationLike;
import ploting_server.ploting.organization.entity.OrganizationMember;
import ploting_server.ploting.organization.repository.OrganizationLikeRepository;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.point.entity.LevelType;
import ploting_server.ploting.point.entity.Point;
import ploting_server.ploting.point.repository.PointRepository;
import ploting_server.ploting.post.entity.PostLike;
import ploting_server.ploting.post.repository.PostLikeRepository;

import java.util.List;

/**
 * 회원을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationLikeRepository organizationLikeRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingLikeRepository meetingLikeRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PointRepository pointRepository;

    /**
     * 회원 가입 시 회원 정보를 추가적으로 등록합니다.
     */
    @Transactional
    public void registerMemberInfo(Long memberId, MemberRegisterRequest memberRegisterRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        if (memberRepository.existsByNickname(memberRegisterRequest.getNickname())) {
            throw new MemberException(MemberErrorCode.DUPLICATE_NICKNAME);
        }

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
                .name(findMember.getName())
                .nickname(findMember.getNickname())
                .location(findMember.getLocation())
                .gender(findMember.getGender())
                .birth(findMember.getBirth())
                .level(findMember.getLevel())
                .levelType(LevelType.findLevelTypeByLevel(findMember.getLevel()))
                .createdAt(findMember.getCreatedAt())
                .build();
    }

    /**
     * 회원 닉네임 중복 여부를 확인합니다.
     */
    @Transactional(readOnly = true)
    public MemberNicknameDuplicationResponse checkNicknameDuplicate(String nickname) {
        Boolean existsByNickname = memberRepository.existsByNickname(nickname);

        return MemberNicknameDuplicationResponse.builder()
                .nicknameDuplicated(existsByNickname)
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
     * 회원 탈퇴합니다.
     */
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 단체 삭제, 단체장인 단체가 있으면 탈퇴할 수 없음
        List<OrganizationMember> organizationMembers = organizationMemberRepository.findAllByMemberId(memberId);
        for (OrganizationMember organizationMember : organizationMembers) {
            if (organizationMember.isLeaderStatus()) {
                throw new OrganizationException(OrganizationErrorCode.CANNOT_WITHDRAW_AS_ORGANIZATION_LEADER);
            }
            organizationMemberRepository.delete(organizationMember);
        }

        // 모임 삭제, 모임장인 모임이 있으면 탈퇴할 수 없음
        List<MeetingMember> meetingMembers = meetingMemberRepository.findAllByMemberId(memberId);
        for (MeetingMember meetingMember : meetingMembers) {
            if (meetingMember.isLeaderStatus()) {
                throw new MeetingException(MeetingErrorCode.CANNOT_WITHDRAW_AS_MEETING_LEADER);
            }
            meetingMemberRepository.delete(meetingMember);
        }

        // 단체 좋아요 삭제
        List<OrganizationLike> organizationLikes = organizationLikeRepository.findAllByMemberId(memberId);
        organizationLikeRepository.deleteAll(organizationLikes);

        // 모임 좋아요 삭제
        List<MeetingLike> meetingLikes = meetingLikeRepository.findAllByMemberId(memberId);
        meetingLikeRepository.deleteAll(meetingLikes);

        // 게시글 좋아요 삭제
        List<PostLike> postLikes = postLikeRepository.findAllByMemberId(memberId);
        postLikeRepository.deleteAll(postLikes);

        // 댓글 좋아요 삭제
        List<CommentLike> commentLikes = commentLikeRepository.findAllByMemberId(memberId);
        commentLikeRepository.deleteAll(commentLikes);

        // 포인트 삭제
        List<Point> points = pointRepository.findAllByMemberId(memberId);
        pointRepository.deleteAll(points);

        memberRepository.delete(member);
    }
}
