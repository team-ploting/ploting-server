package ploting_server.ploting.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.meeting.entity.MeetingLike;

import java.util.List;
import java.util.Optional;

/**
 * MeetingLike의 Repository 입니다.
 */
public interface MeetingLikeRepository extends JpaRepository<MeetingLike, Long> {
    List<MeetingLike> findAllByMemberId(Long memberId);

    List<MeetingLike> findAllByMeetingId(Long meetingId);

    boolean existsByMemberIdAndMeetingId(Long memberId, Long meetingId);

    Optional<MeetingLike> findByMemberIdAndMeetingId(Long memberId, Long meetingId);
}
