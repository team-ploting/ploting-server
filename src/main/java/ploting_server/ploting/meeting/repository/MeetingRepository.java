package ploting_server.ploting.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ploting_server.ploting.meeting.entity.Meeting;
import ploting_server.ploting.organization.entity.Organization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 모임 도메인의 Repository 입니다.
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findAllByOrganizationId(Long organizationId);

    List<Meeting> findAllByOrganizationIdAndActiveStatusIsTrue(Long organizationId);

    List<Meeting> findAllByActiveStatusTrueAndMeetDateBefore(LocalDateTime now);

    @Query("SELECT m.organization FROM Meeting m WHERE m.id = :meetingId")
    Optional<Organization> findOrganizationByMeetingId(@Param("meetingId") Long meetingId);

    @Query("SELECT m FROM Meeting m WHERE m.activeStatus = true AND (:search IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Meeting> searchMeetings(@Param("search") String search);

    @Query("select m from Meeting m join m.meetingMembers mm where mm.member.id = :memberId ORDER BY m.createdAt DESC")
    List<Meeting> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}
