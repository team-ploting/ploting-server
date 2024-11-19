package ploting_server.ploting.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.MissionErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.MissionException;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.mission.entity.Mission;
import ploting_server.ploting.mission.repository.MissionRepository;
import ploting_server.ploting.point.entity.Point;
import ploting_server.ploting.point.repository.PointRepository;

/**
 * 포인트를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;

    /**
     * 포인트를 받습니다.
     */
    @Transactional
    public void receivePoints(Long memberId, Long missionId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.NOT_FOUND_MISSION_ID));

        Point point = Point.builder()
                .mission(mission)
                .member(member)
                .build();

        pointRepository.save(point);
    }
}
