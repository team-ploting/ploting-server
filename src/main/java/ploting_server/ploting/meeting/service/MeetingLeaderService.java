package ploting_server.ploting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MeetingErrorCode;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.OrganizationErrorCode;
import ploting_server.ploting.core.exception.MeetingException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.OrganizationException;
import ploting_server.ploting.meeting.dto.request.MeetingCreateRequest;
import ploting_server.ploting.meeting.dto.request.MeetingUpdateRequest;
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
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 모임을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MeetingLeaderService {

    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingLikeRepository meetingLikeRepository;
    private final MemberRepository memberRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    /**
     * 모임을 생성합니다. (모임장만 가능)
     */
    @Transactional
    public void createMeeting(Long memberId, Long organizationId, MeetingCreateRequest meetingCreateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 모임 생성 조건 확인
        checkMeetingCreateCondition(
                member,
                meetingCreateRequest.getMinAge(),
                meetingCreateRequest.getMaxAge(),
                meetingCreateRequest.getMinLevel()
        );

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
     * 모임을 수정합니다. (모임장만 가능)
     */
    @Transactional
    public void updateMeeting(Long memberId, Long meetingId, MeetingUpdateRequest meetingUpdateRequest) {
        // 모임의 모임장인지 확인
        checkMeetingLeader(memberId, meetingId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        // 최대 멤버 제한이 현재 멤버 수보다 작을 수 없음
        if (meeting.getMemberCount() > meetingUpdateRequest.getMaxMember()) {
            throw new MeetingException(MeetingErrorCode.INVALID_MEMBER_LIMIT);
        }

        meeting.updateMeeting(meetingUpdateRequest);
    }

    /**
     * 모임을 삭제합니다. (모임장만 가능)
     */
    @Transactional
    public void deleteMeeting(Long memberId, Long meetingId) {
        // 모임의 모임장인지 확인
        checkMeetingLeader(memberId, meetingId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        // 멤버 수가 1명(모임장)이 아닐 경우 모임을 삭제할 수 없음
        if (meeting.getMemberCount() > 1) {
            throw new MeetingException(MeetingErrorCode.CANNOT_DELETE_MEETING);
        }

        // 모임의 좋아요 삭제
        meetingLikeRepository.deleteAll(meetingLikeRepository.findAllByMeetingId(meetingId));

        // 모임 삭제
        meetingRepository.delete(meeting);
    }

    /**
     * 모임의 멤버를 강퇴합니다. (모임장만 가능)
     */
    @Transactional
    public void banishMember(Long memberId, Long meetingId, Long kickMemberId) {
        // 모임장 권한 확인
        checkMeetingLeader(memberId, meetingId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        MeetingMember kickMember = meetingMemberRepository.findByMeetingIdAndMemberId(meetingId, kickMemberId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_MEETING_MEMBER));

        // 모임장 스스로를 강퇴할 수 없음
        if (kickMember.isLeaderStatus()) {
            throw new MeetingException(MeetingErrorCode.CANNOT_KICK_SELF_LEADER);
        }

        // 멤버 강퇴
        meetingMemberRepository.delete(kickMember);

        // 양방향 연관관계 해제
        meeting.removeMeetingMember(kickMember);
    }

    /**
     * 만료된 모임의 activeStatus를 비활성화로 변경합니다.
     */
    @Transactional
    public void deactivateExpiredMeetings() {
        List<Meeting> expiredMeetings = meetingRepository.findAllByActiveStatusTrueAndMeetDateBefore(LocalDateTime.now());
        for (Meeting meeting : expiredMeetings) {
            meeting.deactivate();
        }
    }

    /**
     * 단체에 속해있는지 확인합니다.
     */
    private void checkBelongToOrganization(Long memberId, Long organizationId) {
        organizationMemberRepository.findByOrganizationIdAndMemberId(organizationId, memberId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_ORGANIZATION_MEMBER));
    }

    /**
     * 모임의 모임장인지 확인합니다.
     */
    private void checkMeetingLeader(Long memberId, Long meetingId) {
        MeetingMember meetingMember = meetingMemberRepository.findByMeetingIdAndLeaderStatusTrue(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        // 모임의 모임장인지 확인
        if (!meetingMember.getMember().getId().equals(memberId)) {
            throw new MeetingException(MeetingErrorCode.NOT_MEETING_LEADER);
        }
    }

    /**
     * 모임 생성 조건을 충족하는지 확인합니다.
     */
    private void checkMeetingCreateCondition(Member member, int minAge, int maxAge, int minLevel) {
        int age = Period.between(member.getBirth(), LocalDate.now()).getYears();
        if (age < minAge || age > maxAge) {
            throw new MeetingException(MeetingErrorCode.AGE_NOT_ELIGIBLE);
        }
        if (member.getLevel() < minLevel) {
            throw new MeetingException(MeetingErrorCode.LEVEL_NOT_ELIGIBLE);
        }
    }
}
