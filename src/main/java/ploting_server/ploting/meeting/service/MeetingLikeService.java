package ploting_server.ploting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MeetingErrorCode;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.MeetingException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.meeting.entity.MeetingLike;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;

/**
 * 모임 좋아요를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MeetingLikeService {

    private final MeetingRepository meetingRepository;
    private final MeetingLikeRepository meetingLikeRepository;
    private final MemberRepository memberRepository;

    /**
     * 모임의 좋아요를 누릅니다.
     */
    @Transactional
    public void pressMeetingLike(Long memberId, Long meetingId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        MeetingLike meetingLike = MeetingLike.builder()
                .meeting(meeting)
                .member(member)
                .build();

        // 좋아요 수 증가
        meeting.incrementLikeCount();

        meetingLikeRepository.save(meetingLike);
    }

    /**
     * 모임의 좋아요를 취소합니다.
     */
    @Transactional
    public void cancelMeetingLike(Long memberId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_ID));

        MeetingLike meetingLike = meetingLikeRepository.findByMemberIdAndMeetingId(memberId, memberId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOT_FOUND_MEETING_LIKE));

        // 좋아요 수 감소
        meeting.decrementLikeCount();

        meetingLikeRepository.delete(meetingLike);
    }
}
