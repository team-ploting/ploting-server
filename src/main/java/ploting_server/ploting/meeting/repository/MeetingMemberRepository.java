package ploting_server.ploting.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.meeting.entity.MeetingMember;

/**
 * MeetingMember의 Repository 입니다.
 */
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
}
