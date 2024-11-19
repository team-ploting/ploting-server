package ploting_server.ploting.mission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.mission.dto.request.MissionCreateRequest;
import ploting_server.ploting.mission.entity.Mission;
import ploting_server.ploting.mission.repository.MissionRepository;

/**
 * 미션을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    /**
     * 미션을 생성합니다.
     */
    @Transactional
    public void createMission(MissionCreateRequest missionCreateRequest) {
        Mission mission = Mission.builder()
                .name(missionCreateRequest.getName())
                .description(missionCreateRequest.getDescription())
                .point(missionCreateRequest.getPoint())
                .build();

        missionRepository.save(mission);
    }
}
