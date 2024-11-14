package ploting_server.ploting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MeetingErrorCode;
import ploting_server.ploting.core.exception.MeetingException;
import ploting_server.ploting.meeting.dto.response.MeetingInfoResponse;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.dto.response.MeetingMemberListResponse;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.meeting.entity.MeetingMember;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingMemberRepository;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;

import java.util.List;

/**
 * 모임을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingLikeRepository meetingLikeRepository;
    private final MemberRepository memberRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    /**
     * 모든 모임 목록을 조회합니다. (페이징 처리)
     */
    @Transactional(readOnly = true)
    public Page<MeetingListResponse> getAllMeetings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Meeting> meetingPage = meetingRepository.findAll(pageable);

        return meetingPage.map(meeting -> MeetingListResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .location(meeting.getLocation())
                .maxMember(meeting.getMaxMember())
                .minAge(meeting.getMinAge())
                .maxAge(meeting.getMaxAge())
                .minLevel(meeting.getMinLevel())
                .memberCount(meeting.getMemberCount())
                .maleCount(meeting.getMaleCount())
                .femaleCount(meeting.getFemaleCount())
                .meetDate(String.join("-",
                        String.valueOf(meeting.getMeetDate().getYear()),
                        String.valueOf(meeting.getMeetDate().getMonthValue()),
                        String.valueOf(meeting.getMeetDate().getDayOfMonth())))
                .build());
    }

    /**
     * 모임 세부 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public MeetingInfoResponse getMeetingInfo(Long memberId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        // 모임이 속해 있는 단체
        Organization organization = meetingRepository.findOrganizationByMeetingId(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        // 가입된 순으로 세명의 멤버 조회
        List<MeetingMember> top3MembersByCreatedAt = meetingMemberRepository.findTop3ByMeetingIdOrderByCreatedAtAsc(meetingId);

        // 사용자가 좋아요를 누른 모임인지 조회
        boolean hasLiked = meetingLikeRepository.existsByMemberIdAndMeetingId(memberId, meetingId);

        return MeetingInfoResponse.builder()
                .name(meeting.getName())
                .location(meeting.getLocation())
                .description(meeting.getDescription())
                .maxMember(meeting.getMaxMember())
                .minAge(meeting.getMinAge())
                .maxAge(meeting.getMaxAge())
                .minLevel(meeting.getMinLevel())
                .meetDate(meeting.getMeetDate())
                .likeCount(meeting.getLikeCount())
                .hasLiked(hasLiked)
                .memberCount(meeting.getMemberCount())
                .maleCount(meeting.getMaleCount())
                .femaleCount(meeting.getFemaleCount())
                .activeStatus(meeting.isActiveStatus())
                .top3Members(top3MembersByCreatedAt)
                .organizationName(organization.getName())
                .organizationMemberCount(organization.getMemberCount())
                .build();
    }

    /**
     * 모임의 멤버를 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<MeetingMemberListResponse> getMeetingMembers(Long meetingId) {
        // 모임의 멤버 리스트 조회
        List<MeetingMember> meetingMembers = meetingMemberRepository.findAllByMeetingId(meetingId);

        // 모임_멤버 리스트를 MeetingMemberListResponse 리스트로 변환
        return meetingMembers.stream()
                .map(meetingMember -> MeetingMemberListResponse.builder()
                        .nickname(meetingMember.getMember().getNickname())
                        .level(meetingMember.getMember().getLevel())
                        .introduction(meetingMember.getIntroduction())
                        .leaderStatus(meetingMember.isLeaderStatus())
                        .build())
                .toList();
    }
}
