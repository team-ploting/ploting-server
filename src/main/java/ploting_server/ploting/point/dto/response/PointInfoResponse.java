package ploting_server.ploting.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ploting_server.ploting.point.entity.LevelType;

/**
 * 포인트 수 및 레벨 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class PointInfoResponse {

    @Schema(description = "현재 레벨에서의 포인트", example = "10")
    private final int currentLevelPoints;

    @Schema(description = "다음 레벨까지 포인트", example = "30")
    private final int nextLevelPoints;

    @Schema(description = "레벨", example = "3")
    private final int level;

    @Schema(description = "레벨 타입", example = "씨앗")
    private final LevelType levelType;

    @Schema(description = "포인트 합계", example = "100")
    private final int totalPoints;
}
