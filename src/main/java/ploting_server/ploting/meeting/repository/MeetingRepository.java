package ploting_server.ploting.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.organization.entity.Organization;

import java.util.List;
import java.util.Optional;

/**
 * 모임 도메인의 Repository 입니다.
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findAllByOrganizationId(Long organizationId);

    List<Meeting> findAllByOrganizationIdAndActiveStatusIsTrue(Long organizationId);

    @Query("SELECT m.organization FROM Meeting m WHERE m.id = :meetingId")
    Optional<Organization> findOrganizationByMeetingId(@Param("meetingId") Long meetingId);
}
