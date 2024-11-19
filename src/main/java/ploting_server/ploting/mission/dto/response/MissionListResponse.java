package ploting_server.ploting.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 미션 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class MissionListResponse {

    @Schema(description = "미션 id", example = "1")
    private final Long id;

    @Schema(description = "미션 이름", example = "텀블러 사용하기")
    private final String name;

    @Schema(description = "미션 포인트", example = "10")
    private final int point;
}
