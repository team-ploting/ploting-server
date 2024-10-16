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
import ploting_server.ploting.organization.dto.request.OrganizationCreateRequest;
import ploting_server.ploting.organization.dto.response.OrganizationInfoResponse;
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
                .femaleCount(0)
                .maleCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        organizationRepository.save(organization);

        // 리더로서 OrganizationMember 생성
        OrganizationMember organizationMember = OrganizationMember.builder()
                .organization(organization)
                .member(member)
                .introduction(organizationCreateRequest.getIntroduction())
                .leaderStatus(true)
                .createdAt(LocalDateTime.now())
                .build();

        organizationMemberRepository.save(organizationMember);
    }

    /**
     * 단체 세부 정보를 조회합니다.
     */
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
}
