package ploting_server.ploting.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.meeting.entity.MeetingLike;

import java.util.List;

/**
 * MeetingLike의 Repository 입니다.
 */
public interface MeetingLikeRepository extends JpaRepository<MeetingLike, Long> {
    List<MeetingLike> findAllByMeetingId(Long meetingId);

}
