package ploting_server.ploting.organization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.OrganizationErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.OrganizationException;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.dto.request.OrganizationCreateRequest;
import ploting_server.ploting.organization.dto.request.OrganizationUpdateRequest;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.organization.entity.OrganizationMember;
import ploting_server.ploting.organization.repository.OrganizationLikeRepository;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;

import java.util.List;

/**
 * 단체를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class OrganizationLeaderService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationLikeRepository organizationLikeRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingLikeRepository meetingLikeRepository;

    /**
     * 단체를 생성합니다.
     */
    @Transactional
    public void createOrganization(Long memberId, OrganizationCreateRequest organizationCreateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 단체 생성
        Organization organization = Organization.builder()
                .name(organizationCreateRequest.getName())
                .description(organizationCreateRequest.getDescription())
                .location(organizationCreateRequest.getLocation())
                .maxMember(organizationCreateRequest.getMaxMember())
                .minAge(organizationCreateRequest.getMinAge())
                .maxAge(organizationCreateRequest.getMaxAge())
                .minLevel(organizationCreateRequest.getMinLevel())
                .likeCount(0)
                .memberCount(0)
                .maleCount(0)
                .femaleCount(0)
                .build();

        organizationRepository.save(organization);

        // 리더로서 OrganizationMember 생성
        OrganizationMember organizationMember = OrganizationMember.builder()
                .organization(organization)
                .member(member)
                .leaderStatus(true)
                .build();

        organizationMemberRepository.save(organizationMember);

        // 양방향 연관관계 설정
        organization.addOrganizationMember(organizationMember);
    }

    /**
     * 단체를 수정합니다. (단체장만 가능)
     */
    @Transactional
    public void updateOrganization(Long memberId, Long organizationId, OrganizationUpdateRequest organizationUpdateRequest) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        OrganizationMember organizationMember = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체장 권한 확인
        checkOrganizationLeader(memberId, organizationMember);

        // 최대 멤버 제한이 현재 멤버 수보다 작을 수 없음
        if (organization.getMemberCount() > organizationUpdateRequest.getMaxMember()) {
            throw new OrganizationException(OrganizationErrorCode.INVALID_MEMBER_LIMIT);
        }

        // 단체 수정
        organization.updateOrganization(organizationUpdateRequest);
    }

    /**
     * 단체를 삭제합니다. (단체장만 가능)
     */
    @Transactional
    public void deleteOrganization(Long memberId, Long organizationId) {
        // 단체장 권한 확인
        OrganizationMember leader = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        checkOrganizationLeader(memberId, leader);

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 멤버 수가 1명(단체장)이 아닐 경우 단체를 삭제할 수 없음
        if (organization.getMemberCount() > 1) {
            throw new OrganizationException(OrganizationErrorCode.CANNOT_DELETE_ORGANIZATION_WITH_MULTIPLE_MEMBERS);
        }

        // 활성화된 모임 수가 1개 이상일 경우 단체를 삭제할 수 없음
        if (meetingRepository.existsByOrganizationIdAndActiveStatusIsTrue(organizationId)) {
            throw new OrganizationException(OrganizationErrorCode.CANNOT_DELETE_ORGANIZATION_WITH_EXISTING_MEETINGS);
        }

        // 모임 삭제 및 좋아요 삭제
        List<Meeting> meetings = meetingRepository.findAllByOrganizationId(organizationId);
        for (Meeting meeting : meetings) {
            meetingLikeRepository.deleteAll(meetingLikeRepository.findAllByMeetingId(meeting.getId()));
        }
        meetingRepository.deleteAll(meetings);

        // 단체의 좋아요 삭제
        organizationLikeRepository.deleteAll(organizationLikeRepository.findAllByOrganizationId(organizationId));

        // 단체 삭제
        organizationRepository.delete(organization);
    }

    /**
     * 단체장 권한을 다른 회원에게 위임합니다. (단체장만 가능)
     */
    @Transactional
    public void delegateLeader(Long memberId, Long organizationId, Long newLeaderId) {
        // 현재 단체장
        OrganizationMember currentLeader = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체장 권한 확인
        checkOrganizationLeader(memberId, currentLeader);

        // 새로운 단체장
        OrganizationMember newLeader = organizationMemberRepository.findByOrganizationIdAndMemberId(organizationId, newLeaderId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_MEMBER));

        // 기존 단체장의 권한을 해제
        currentLeader.revokeLeader();

        // 새로운 단체장에게 권한을 부여
        newLeader.assignLeader();
    }

    /**
     * 단체의 멤버를 강퇴합니다. (단체장만 가능)
     */
    @Transactional
    public void banishMember(Long memberId, Long organizationId, Long kickMemberId) {
        OrganizationMember organizationMember = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체장 권한 확인
        checkOrganizationLeader(memberId, organizationMember);

        Member kickMember = memberRepository.findById(kickMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체장 스스로를 강퇴할 수 없음
        if (organizationMember.isLeaderStatus()) {
            throw new OrganizationException(OrganizationErrorCode.CANNOT_KICK_SELF_LEADER);
        }

        // 멤버 강퇴
        organizationMemberRepository.delete(organizationMember);

        // 양방향 연관관계 해제
        organization.removeOrganizationMember(organizationMember);
    }

    /**
     * 단체의 단체장인지 확인합니다.
     */
    private void checkOrganizationLeader(Long memberId, OrganizationMember organizationMember) {
        // 단체의 단체장인지 확인
        if (!organizationMember.getMember().getId().equals(memberId)) {
            throw new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_LEADER);
        }
    }
}
