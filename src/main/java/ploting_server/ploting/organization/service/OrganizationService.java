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
import ploting_server.ploting.member.entity.GenderType;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.dto.request.OrganizationCreateRequest;
import ploting_server.ploting.organization.dto.request.OrganizationUpdateRequest;
import ploting_server.ploting.organization.dto.response.OrganizationInfoResponse;
import ploting_server.ploting.organization.dto.response.OrganizationListResponse;
import ploting_server.ploting.organization.dto.response.OrganizationMemberListResponse;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.organization.entity.OrganizationMember;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 단체를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;

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
                .organizationImageUrl(organizationCreateRequest.getOrganizationImageUrl())
                .likeCount(0)
                .memberCount(0)
                .maleCount(0)
                .femaleCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        organizationRepository.save(organization);

        // 성별 수 증가
        if (member.getGender().equals(GenderType.MALE)) {
            organization.incrementMaleCount();
        }

        if (member.getGender().equals(GenderType.FEMALE)) {
            organization.incrementFemaleCount();
        }

        // 멤버 수 증가
        organization.incrementMemberCount();

        // 리더로서 OrganizationMember 생성
        OrganizationMember organizationMember = OrganizationMember.builder()
                .organization(organization)
                .member(member)
                .leaderStatus(true)
                .createdAt(LocalDateTime.now())
                .build();

        // 양방향 연관관계 설정
        organization.addOrganizationMember(organizationMember);

        organizationMemberRepository.save(organizationMember);
    }

    /**
     * 모든 단체 목록을 조회합니다. (페이징 처리)
     */
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
    public OrganizationInfoResponse getOrganizationInfo(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        OrganizationMember organizationMember = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

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
                .organizationImageUrl(organization.getOrganizationImageUrl())
                .likeCount(organization.getLikeCount())
                .memberCount(organization.getMemberCount())
                .leaderName(organizationMember.getMember().getName())
                .leaderLevel(organizationMember.getMember().getLevel())
                .maleCount(organization.getMaleCount())
                .femaleCount(organization.getFemaleCount())
                .meetingListResponse(meetingListResponse)
                .build();
    }

    /**
     * 단체를 수정합니다. (단체장만 가능)
     */
    @Transactional
    public void updateOrganization(Long memberId, Long organizationId, OrganizationUpdateRequest organizationUpdateRequest) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체의 단체장인지 확인
        checkOrganizationLeader(memberId, organizationId);

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
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체의 단체장인지 확인
        checkOrganizationLeader(memberId, organizationId);

        // 멤버 수가 1명(단체장)이 아닐 경우 단체를 삭제할 수 없음
        if (organization.getMemberCount() != 1) {
            throw new OrganizationException(OrganizationErrorCode.CANNOT_DELETE_ORGANIZATION);
        }

        // 단체 삭제
        organizationRepository.delete(organization);
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
                        .profileImageUrl(organizationMember.getMember().getProfileImageUrl())
                        .introduction(organizationMember.getIntroduction())
                        .leaderStatus(organizationMember.isLeaderStatus())
                        .build())
                .toList();
    }

    /**
     * 단체장 권한을 다른 회원에게 위임합니다. (단체장만 가능)
     */
    @Transactional
    public void delegateLeader(Long memberId, Long organizationId, Long newLeaderId) {
        // 현재 단체장
        OrganizationMember currentLeader = checkOrganizationLeader(memberId, organizationId);

        // 새로운 단체장
        OrganizationMember newLeader = organizationMemberRepository.findByOrganizationIdAndMemberId(organizationId, newLeaderId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_MEMBER));

        // 기존 단체장의 권한을 해제
        currentLeader.revokeLeader();

        // 새로운 단체장에게 권한을 부여
        newLeader.assignLeader();
    }

    /**
     * 단체의 멤버를 강퇴합니다.
     */
    @Transactional
    public void kickMember(Long memberId, Long organizationId, Long kickMemberId) {
        // 단체장 권한 확인
        checkOrganizationLeader(memberId, organizationId);

        OrganizationMember organizationMember = organizationMemberRepository.findByOrganizationIdAndMemberId(organizationId, kickMemberId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_MEMBER));

        // 멤버 강퇴
        organizationMemberRepository.delete(organizationMember);
    }

    /**
     * 단체의 단체장인지 확인합니다.
     */
    private OrganizationMember checkOrganizationLeader(Long memberId, Long organizationId) {
        OrganizationMember organizationMember = organizationMemberRepository.findByOrganizationIdAndLeaderStatusTrue(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        // 단체의 단체장인지 확인
        if (!organizationMember.getMember().getId().equals(memberId)) {
            throw new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_LEADER);
        }

        return organizationMember;
    }
}
