package ploting_server.ploting.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.meeting.entity.MeetingMember;

import java.util.List;
import java.util.Optional;

/**
 * MeetingMember의 Repository 입니다.
 */
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    Optional<MeetingMember> findByMeetingIdAndLeaderStatusTrue(Long meetingId);

    Optional<MeetingMember> findByMeetingIdAndMemberId(Long meetingId, Long memberId);

    List<MeetingMember> findTop3ByMeetingIdOrderByCreatedAtAsc(Long meetingId);
}
