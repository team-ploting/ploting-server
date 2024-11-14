package ploting_server.ploting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingMemberRepository;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;

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
}
