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
import ploting_server.ploting.point.dto.response.PointInfoResponse;
import ploting_server.ploting.point.dto.response.PointListResponse;
import ploting_server.ploting.point.entity.GrassType;
import ploting_server.ploting.point.entity.LevelType;
import ploting_server.ploting.point.entity.Point;
import ploting_server.ploting.point.repository.PointRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 포인트를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;

    private static final int LEVEL_UP_UNIT = 30;

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

        // 포인트 갱신
        Integer totalPointSum = pointRepository.findTotalPointByMemberId(memberId);
        int totalPoints = totalPointSum != null ? totalPointSum : 0;
        member.updateLevel((totalPoints - 1) / LEVEL_UP_UNIT + 1);
    }

    /**
     * 포인트 수 및 레벨 조회
     */
    @Transactional(readOnly = true)
    public PointInfoResponse getMyPointAndLevel(Long memberId) {
        Integer totalPointSum = pointRepository.findTotalPointByMemberId(memberId);
        int totalPoints = totalPointSum != null ? totalPointSum : 0;

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        return PointInfoResponse.builder()
                .currentLevelPoints(totalPoints % LEVEL_UP_UNIT)
                .nextLevelPoints(LEVEL_UP_UNIT)
                .level(member.getLevel())
                .levelType(LevelType.findLevelTypeByLevel(member.getLevel()))
                .totalPoints(totalPoints)
                .build();
    }

    /**
     * 날짜별 포인트 수를 조회합니다. (잔디)
     */
    @Transactional(readOnly = true)
    public List<PointListResponse> getPointsByDate(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = now.with(TemporalAdjusters.lastDayOfMonth());

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        List<Object[]> points = pointRepository.findPointsByDate(memberId, startDate, endDate);

        Map<Integer, PointListResponse> dateMap = new HashMap<>();
        for (int day = 1; day <= endDate.getDayOfMonth(); day++) {
            dateMap.put(day, PointListResponse.builder()
                    .date(day)
                    .grassLevel(GrassType.ZERO.getStep())
                    .build());
        }

        for (Object[] point : points) {
            LocalDate date = ((Date) point[0]).toLocalDate();
            Number totalPoints = (Number) point[1];

            dateMap.put(date.getDayOfMonth(), PointListResponse.builder()
                    .date(date.getDayOfMonth())
                    .grassLevel(GrassType.findGrassStepByPoint(totalPoints.intValue()))
                    .build());
        }

        return dateMap.values().stream()
                .sorted(Comparator.comparing(PointListResponse::getDate))
                .toList();
    }
}
