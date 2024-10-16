package ploting_server.ploting.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.meeting.entity.Meeting;

import java.util.List;

/**
 * 모임 도메인의 Repository 입니다.
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findAllByOrganizationId(Long organizationId);
}
