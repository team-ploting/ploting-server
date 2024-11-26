package ploting_server.ploting.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ploting_server.ploting.point.entity.Point;

import java.time.LocalDate;
import java.util.List;

/**
 * 포인트 도메인의 Repository 입니다.
 */
public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT DATE(p.createdAt) as date, SUM(p.mission.point) as totalPoints " +
            "FROM Point p " +
            "WHERE p.member.id = :memberId " +
            "AND DATE(p.createdAt) BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(p.createdAt) " +
            "ORDER BY DATE(p.createdAt) ASC")
    List<Object[]> findPointsByDate(@Param("memberId") Long memberId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(p.mission.point) FROM Point p WHERE p.member.id = :memberId")
    Integer findTotalPointByMemberId(@Param("memberId") Long memberId);

    List<Point> findAllByMemberId(Long memberId);
}
