package ploting_server.ploting.mission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MissionErrorCode;
import ploting_server.ploting.core.exception.MissionException;
import ploting_server.ploting.mission.dto.request.MissionCreateRequest;
import ploting_server.ploting.mission.dto.response.MissionListResponse;
import ploting_server.ploting.mission.entity.Mission;
import ploting_server.ploting.mission.repository.MissionRepository;

import java.util.List;

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

    /**
     * 미션을 삭제합니다.
     */
    @Transactional
    public void deleteMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.NOT_FOUND_MISSION_ID));

        missionRepository.delete(mission);
    }

    /**
     * 모든 미션을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<MissionListResponse> getAllMissions() {
        List<Mission> missions = missionRepository.findAll();

        return missions.stream()
                .map(mission -> MissionListResponse.builder()
                        .id(mission.getId())
                        .name(mission.getName())
                        .point(mission.getPoint())
                        .build())
                .toList();
    }
}
