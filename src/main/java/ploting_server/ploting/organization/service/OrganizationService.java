package ploting_server.ploting.organization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationLikeRepository organizationLikeRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;

    /**
     * 모든 단체 목록을 조회합니다. (페이징 처리)
     */
    @Transactional(readOnly = true)
    public Page<OrganizationListResponse> getAllOrganizationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Organization> organizationPage = organizationRepository.findAll(pageable);

        return organizationPage.map(organization -> OrganizationListResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .description(organization.getDescription())
                .location(organization.getLocation())
                .minAge(organization.getMinAge())
                .maxAge(organization.getMaxAge())
                .minLevel(organization.getMinLevel())
                .memberCount(organization.getMemberCount())
                .maleCount(organization.getMaleCount())
                .femaleCount(organization.getFemaleCount())
                .build());
    }

    /**
     * 나의 단체 목록을 조회합니다. (페이징 처리)
     */
    @Transactional(readOnly = true)
    public Page<OrganizationListResponse> getMyOrganizationList(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Organization> organizationPage = organizationRepository.findAllByMemberId(memberId, pageable);

        return organizationPage.map(organization -> OrganizationListResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .description(organization.getDescription())
                .location(organization.getLocation())
                .minAge(organization.getMinAge())
                .maxAge(organization.getMaxAge())
                .minLevel(organization.getMinLevel())
                .memberCount(organization.getMemberCount())
                .maleCount(organization.getMaleCount())
                .femaleCount(organization.getFemaleCount())
                .build());
    }

    /**
     * 단체 세부 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public OrganizationInfoResponse getOrganizationInfo(Long memberId, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        OrganizationMember organizationMember = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        boolean hasLiked = organizationLikeRepository.existsByMemberIdAndOrganizationId(memberId, organizationId);

        // 단체의 모임 리스트 조회
        List<Meeting> meetingList = meetingRepository.findAllByOrganizationId(organizationId);

        // 모임 리스트를 MeetingListResponse 리스트로 변환
        List<MeetingListResponse> meetingListResponse = meetingList.stream()
                .map(meeting -> MeetingListResponse.builder()
                        .name(meeting.getName())
                        .location(meeting.getLocation())
                        .maxMember(meeting.getMaxMember())
                        .minAge(meeting.getMinAge())
                        .maxAge(meeting.getMaxAge())
                        .minLevel(meeting.getMinLevel())
                        .memberCount(meeting.getMemberCount())
                        .maleCount(meeting.getMaleCount())
                        .femaleCount(meeting.getFemaleCount())
                        .meetDate(meeting.getMeetDate())
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
                .hasLiked(hasLiked)
                .memberCount(organization.getMemberCount())
                .leaderName(organizationMember.getMember().getNickname())
                .leaderLevel(organizationMember.getMember().getLevel())
                .maleCount(organization.getMaleCount())
                .femaleCount(organization.getFemaleCount())
                .meetingListResponse(meetingListResponse)
                .build();
    }

    /**
     * 단체의 멤버를 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<OrganizationMemberListResponse> getOrganizationMemberList(Long organizationId) {
        // 단체의 멤버 리스트 조회
        List<OrganizationMember> organizationMemberList = organizationMemberRepository.findAllByOrganizationId(organizationId);

        // 모임_멤버 리스트를 OrganizationMemberInfoResponse 리스트로 변환
        return organizationMemberList.stream()
                .map(organizationMember -> OrganizationMemberListResponse.builder()
                        .nickname(organizationMember.getMember().getNickname())
                        .level(organizationMember.getMember().getLevel())
                        .introduction(organizationMember.getIntroduction())
                        .leaderStatus(organizationMember.isLeaderStatus())
                        .build())
                .toList();
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
    }

    /**
     * 단체를 탈퇴합니다.
     */
    @Transactional
    public void departOrganization(Long memberId, Long organizationId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

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

        // 멤버 수, 성별 수 감소
        organization.decrementMemberAndGenderCount(member.getGender());

        organizationMemberRepository.delete(organizationMember);
    }
}
