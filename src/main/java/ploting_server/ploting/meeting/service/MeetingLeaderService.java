package ploting_server.ploting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.OrganizationErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.OrganizationException;
import ploting_server.ploting.meeting.dto.request.MeetingCreateRequest;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.meeting.entity.MeetingMember;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingMemberRepository;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 모임을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MeetingLeaderService {

    private final MeetingRepository meetingRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingLikeRepository meetingLikeRepository;
    private final MemberRepository memberRepository;

    /**
     * 모임을 생성합니다.
     */
    @Transactional
    public void createMeeting(Long memberId, Long organizationId, MeetingCreateRequest meetingCreateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        checkBelongToOrganization(memberId, organizationId);

        LocalDate date = meetingCreateRequest.getMeetDate();
        int hour = meetingCreateRequest.getMeetHour();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        Meeting meeting = Meeting.builder()
                .organization(organization)
                .name(meetingCreateRequest.getName())
                .location(meetingCreateRequest.getLocation())
                .description(meetingCreateRequest.getDescription())
                .maxMember(meetingCreateRequest.getMaxMember())
                .minAge(meetingCreateRequest.getMinAge())
                .maxAge(meetingCreateRequest.getMaxAge())
                .minLevel(meetingCreateRequest.getMinLevel())
                .meetDate(LocalDateTime.parse(date + " " + hour, formatter))
                .likeCount(0)
                .memberCount(0)
                .maleCount(0)
                .femaleCount(0)
                .activeStatus(true)
                .build();

        meetingRepository.save(meeting);

        // 멤버 수, 성별 수 증가
        meeting.incrementMemberAndGenderCount(member.getGender());

        // 모임장으로서 MeetingMember 생성
        MeetingMember meetingMember = MeetingMember.builder()
                .meeting(meeting)
                .member(member)
                .leaderStatus(true)
                .build();

        meetingMemberRepository.save(meetingMember);

        // 양방향 연관관계 설정
        meeting.addMeetingMember(meetingMember);
    }


    /**
     * 단체에 속해있는지 확인합니다.
     */
    private void checkBelongToOrganization(Long memberId, Long organizationId) {

        organizationMemberRepository.findByOrganizationIdAndMemberId(organizationId, memberId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_MEMBER));
    }
}
