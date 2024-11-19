package ploting_server.ploting.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.mission.entity.Mission;

/**
 * 미션 도메인의 Repository 입니다.
 */
public interface MissionRepository extends JpaRepository<Mission, Long> {
}
