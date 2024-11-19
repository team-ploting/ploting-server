package ploting_server.ploting.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 미션 세부 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class MissionInfoResponse {

    @Schema(description = "미션 이름", example = "텀블러 사용하기")
    private final String name;

    @Schema(description = "미션 설명", example = "일회용 컵 대신 텀블러를 사용하고 해당 장면을 사진으로 남겨주세요")
    private final String description;

    @Schema(description = "미션 포인트", example = "10")
    private final int point;
}
