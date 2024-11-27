package ploting_server.ploting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MeetingErrorCode;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.MeetingException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.meeting.dto.response.MeetingInfoResponse;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.dto.response.MeetingMemberListResponse;
import ploting_server.ploting.meeting.dto.response.MeetingMemberResponse;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.meeting.entity.MeetingMember;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingMemberRepository;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.point.entity.LevelType;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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

    /**
     * 모든 모임 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<MeetingListResponse> getAllMeetings() {
        List<Meeting> meetings = meetingRepository.findAllOrderByCreatedAtDesc();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 a h시", Locale.KOREAN);

        return meetings.stream()
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
                .toList();
    }

    /**
     * 나의 모임 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<MeetingListResponse> getMyMeetings(Long memberId) {
        List<Meeting> meetings = meetingRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 a h시", Locale.KOREAN);

        return meetings.stream()
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
                .toList();
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

        List<MeetingMemberListResponse> top3Members = top3MembersByCreatedAt.stream()
                .map(meetingMember -> MeetingMemberListResponse.builder()
                        .nickname(meetingMember.getMember().getNickname())
                        .level(meetingMember.getMember().getLevel())
                        .levelType(LevelType.findLevelTypeByLevel(meetingMember.getMember().getLevel()))
                        .leaderStatus(meetingMember.isLeaderStatus())
                        .build())
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 a h시", Locale.KOREAN);

        return MeetingInfoResponse.builder()
                .name(meeting.getName())
                .location(meeting.getLocation())
                .meetDate(meeting.getMeetDate().format(formatter))
                .description(meeting.getDescription())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .minAge(meeting.getMinAge())
                .maxAge(meeting.getMaxAge())
                .minLevel(meeting.getMinLevel())
                .likeCount(meeting.getLikeCount())
                .hasLiked(meetingLikeRepository.existsByMemberIdAndMeetingId(memberId, meetingId))
                .myMeeting(top3MembersByCreatedAt.get(0).getMember().getId().equals(memberId))
                .top3Members(top3Members)
                .maleCount(meeting.getMaleCount())
                .femaleCount(meeting.getFemaleCount())
                .organizationName(organization.getName())
                .organizationMemberCount(organization.getMemberCount())
                .activeStatus(meeting.isActiveStatus())
                .build();
    }

    /**
     * 모임의 멤버를 조회합니다.
     */
    @Transactional(readOnly = true)
    public MeetingMemberResponse getMeetingMembers(Long meetingId) {
        // 모임의 멤버 리스트 조회
        List<MeetingMember> meetingMembers = meetingMemberRepository.findAllByMeetingId(meetingId);

        // 모임_멤버 리스트를 MeetingMemberListResponse 리스트로 변환
        List<MeetingMemberListResponse> meetingMemberListResponse = meetingMembers.stream()
                .map(meetingMember -> MeetingMemberListResponse.builder()
                        .nickname(meetingMember.getMember().getNickname())
                        .level(meetingMember.getMember().getLevel())
                        .levelType(LevelType.findLevelTypeByLevel(meetingMember.getMember().getLevel()))
                        .leaderStatus(meetingMember.isLeaderStatus())
                        .build())
                .toList();

        return MeetingMemberResponse.builder()
                .memberCount(meetingMemberListResponse.size())
                .members(meetingMemberListResponse)
                .build();
    }

    /**
     * 모임을 가입합니다.
     */
    @Transactional
    public void registerMeeting(Long memberId, Long meetingId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        // 이미 가입된 회원인지 확인
        boolean isMemberExists = meetingMemberRepository.existsByMeetingIdAndMemberId(meetingId, memberId);
        if (isMemberExists) {
            throw new MeetingException(MeetingErrorCode.ALREADY_REGISTERED_MEMBER);
        }

        MeetingMember meetingMember = MeetingMember.builder()
                .meeting(meeting)
                .member(member)
                .leaderStatus(false)
                .build();

        meetingMemberRepository.save(meetingMember);

        // 양방향 연관관계 설정
        meeting.addMeetingMember(meetingMember);
    }

    /**
     * 모임을 탈퇴합니다.
     */
    @Transactional
    public void departMeeting(Long memberId, Long meetingId) {
        MeetingMember meetingMember = meetingMemberRepository.findByMeetingIdAndMemberId(meetingId, memberId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_MEETING_MEMBER));

        // 모임장은 탈퇴할 수 없음
        if (meetingMember.isLeaderStatus()) {
            throw new MeetingException(MeetingErrorCode.CANNOT_LEAVE_ORGANIZATION_AS_LEADER);
        }

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        // 양방향 연관관계 해제
        meeting.removeMeetingMember(meetingMember);

        meetingMemberRepository.delete(meetingMember);
    }
}
