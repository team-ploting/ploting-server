package ploting_server.ploting.organization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.OrganizationErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.OrganizationException;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.dto.response.OrganizationInfoResponse;
import ploting_server.ploting.organization.dto.response.OrganizationListResponse;
import ploting_server.ploting.organization.dto.response.OrganizationMemberListResponse;
import ploting_server.ploting.organization.dto.response.OrganizationMemberResponse;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.organization.entity.OrganizationMember;
import ploting_server.ploting.organization.repository.OrganizationLikeRepository;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;
import ploting_server.ploting.point.entity.LevelType;

import java.util.List;

/**
 * 단체를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationLikeRepository organizationLikeRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;

    /**
     * 모든 단체 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<OrganizationListResponse> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAllOrderByCreatedAtDesc();

        return organizations.stream()
                .map(organization -> OrganizationListResponse.builder()
                        .id(organization.getId())
                        .location(organization.getLocation())
                        .name(organization.getName())
                        .description(organization.getDescription())
                        .minLevel(organization.getMinLevel())
                        .memberCount(organization.getMemberCount())
                        .maxMember(organization.getMaxMember())
                        .minAge(organization.getMinAge())
                        .maxAge(organization.getMaxAge())
                        .maleCount(organization.getMaleCount())
                        .femaleCount(organization.getFemaleCount())
                        .createdAt(organization.getCreatedAt())
                        .build())
                .toList();
    }

    /**
     * 나의 단체 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<OrganizationListResponse> getMyOrganizations(Long memberId) {
        List<Organization> myOrganizations = organizationRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);

        return myOrganizations.stream()
                .map(organization -> OrganizationListResponse.builder()
                        .id(organization.getId())
                        .location(organization.getLocation())
                        .name(organization.getName())
                        .description(organization.getDescription())
                        .minLevel(organization.getMinLevel())
                        .memberCount(organization.getMemberCount())
                        .maxMember(organization.getMaxMember())
                        .minAge(organization.getMinAge())
                        .maxAge(organization.getMaxAge())
                        .maleCount(organization.getMaleCount())
                        .femaleCount(organization.getFemaleCount())
                        .createdAt(organization.getCreatedAt())
                        .build())
                .toList();
    }

    /**
     * 단체 세부 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public OrganizationInfoResponse getOrganizationInfo(Long memberId, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        OrganizationMember leader = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체의 모임 리스트 조회
        List<Meeting> meetingList = meetingRepository.findAllByOrganizationIdOrderByCreatedAtDesc(organizationId);

        // 모임 리스트를 MeetingListResponse 리스트로 변환
        List<MeetingListResponse> meetingListResponse = meetingList.stream()
                .map(meeting -> MeetingListResponse.builder()
                        .id(meeting.getId())
                        .name(meeting.getName())
                        .meetDate(meeting.getMeetDate().toLocalDate())
                        .meetHour(meeting.getMeetDate().getHour())
                        .location(meeting.getLocation())
                        .minLevel(meeting.getMinLevel())
                        .memberCount(meeting.getMemberCount())
                        .maxMember(meeting.getMaxMember())
                        .minAge(meeting.getMinAge())
                        .maxAge(meeting.getMaxAge())
                        .maleCount(meeting.getMaleCount())
                        .femaleCount(meeting.getFemaleCount())
                        .createdAt(meeting.getCreatedAt())
                        .build())
                .toList();

        return OrganizationInfoResponse.builder()
                .name(organization.getName())
                .description(organization.getDescription())
                .location(organization.getLocation())
                .maxMember(organization.getMaxMember())
                .minAge(organization.getMinAge())
                .maxAge(organization.getMaxAge())
                .minLevel(organization.getMinLevel())
                .likeCount(organization.getLikeCount())
                .hasLiked(organizationLikeRepository.existsByMemberIdAndOrganizationId(memberId, organizationId))
                .memberCount(organization.getMemberCount())
                .leaderName(leader.getMember().getNickname())
                .leaderLevel(leader.getMember().getLevel())
                .leaderLevelType(LevelType.findLevelTypeByLevel(leader.getMember().getLevel()))
                .myOrganization(leader.getMember().getId().equals(memberId))
                .maleCount(organization.getMaleCount())
                .femaleCount(organization.getFemaleCount())
                .meetings(meetingListResponse)
                .build();
    }

    /**
     * 단체의 멤버를 조회합니다.
     */
    @Transactional(readOnly = true)
    public OrganizationMemberResponse getOrganizationMembers(Long organizationId) {
        // 단체의 멤버 리스트 조회
        List<OrganizationMember> organizationMembers = organizationMemberRepository.findAllByOrganizationId(organizationId);

        // 단체_멤버 리스트를 OrganizationMemberListResponse 리스트로 변환
        List<OrganizationMemberListResponse> organizationMemberListResponse = organizationMembers.stream()
                .map(organizationMember -> OrganizationMemberListResponse.builder()
                        .nickname(organizationMember.getMember().getNickname())
                        .level(organizationMember.getMember().getLevel())
                        .levelType(LevelType.findLevelTypeByLevel(organizationMember.getMember().getLevel()))
                        .introduction(organizationMember.getIntroduction())
                        .leaderStatus(organizationMember.isLeaderStatus())
                        .build())
                .toList();

        return OrganizationMemberResponse.builder()
                .memberCount(organizationMemberListResponse.size())
                .members(organizationMemberListResponse)
                .build();
    }

    /**
     * 단체를 가입합니다.
     */
    @Transactional
    public void registerOrganization(Long memberId, Long organizationId, String introduction) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 이미 가입된 회원인지 확인
        boolean isMemberExists = organizationMemberRepository.existsByOrganizationIdAndMemberId(organizationId, memberId);
        if (isMemberExists) {
            throw new OrganizationException(OrganizationErrorCode.ALREADY_REGISTERED_MEMBER);
        }

        OrganizationMember organizationMember = OrganizationMember.builder()
                .organization(organization)
                .member(member)
                .introduction(introduction)
                .leaderStatus(false)
                .build();

        organizationMemberRepository.save(organizationMember);

        // 양방향 연관관계 설정
        organization.addOrganizationMember(organizationMember);
    }

    /**
     * 단체를 탈퇴합니다.
     */
    @Transactional
    public void departOrganization(Long memberId, Long organizationId) {
        OrganizationMember organizationMember = organizationMemberRepository.findByOrganizationIdAndMemberId(organizationId, memberId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_MEMBER));

        // 단체장은 탈퇴할 수 없음
        if (organizationMember.isLeaderStatus()) {
            throw new OrganizationException(OrganizationErrorCode.CANNOT_LEAVE_ORGANIZATION_AS_LEADER);
        }

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 양방향 연관관계 해제
        organization.removeOrganizationMember(organizationMember);

        organizationMemberRepository.delete(organizationMember);
    }
}
