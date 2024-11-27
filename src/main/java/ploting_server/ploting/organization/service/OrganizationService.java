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
import ploting_server.ploting.organization.dto.request.OrganizationJoinRequest;
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

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

        // 단체의 최신순 모임 1개 조회
        Optional<Meeting> firstMeeting = meetingRepository.findFirstByOrganizationIdAndActiveStatusTrueOrderByCreatedAtDesc(organizationId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 a h시", Locale.KOREAN);

        MeetingListResponse recentMeeting = firstMeeting
                .map(meeting -> MeetingListResponse.builder()
                        .id(meeting.getId())
                        .activeStatus(meeting.isActiveStatus())
                        .name(meeting.getName())
                        .meetDate(meeting.getMeetDate().format(formatter))
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
                .orElse(null);

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
                .recentMeeting(recentMeeting)
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
    public void registerOrganization(Long memberId, Long organizationId, OrganizationJoinRequest organizationJoinRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체 가입 조건 확인
        checkOrganizationJoinCondition(
                member,
                organization.getMinAge(),
                organization.getMaxAge(),
                organization.getMinLevel(),
                organization.getMemberCount(),
                organization.getMaxMember()
        );

        // 이미 가입된 회원인지 확인
        boolean isMemberExists = organizationMemberRepository.existsByOrganizationIdAndMemberId(organizationId, memberId);
        if (isMemberExists) {
            throw new OrganizationException(OrganizationErrorCode.ALREADY_REGISTERED_MEMBER);
        }

        OrganizationMember organizationMember = OrganizationMember.builder()
                .organization(organization)
                .member(member)
                .introduction(organizationJoinRequest.getIntroduction())
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

    /**
     * 단체 가입 조건을 충족하는지 확인합니다.
     */
    private void checkOrganizationJoinCondition(Member member, int minAge, int maxAge, int minLevel, int memberCount, int maxMember) {
        int age = Period.between(member.getBirth(), LocalDate.now()).getYears();
        if (age < minAge || age > maxAge) {
            throw new OrganizationException(OrganizationErrorCode.AGE_NOT_ELIGIBLE);
        }
        if (member.getLevel() < minLevel) {
            throw new OrganizationException(OrganizationErrorCode.LEVEL_NOT_ELIGIBLE);
        }
        if (memberCount >= maxMember) {
            throw new OrganizationException(OrganizationErrorCode.FULL_MEMBER_CAPACITY);
        }
    }
}
