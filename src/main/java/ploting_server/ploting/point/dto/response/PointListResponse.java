package ploting_server.ploting.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 날짜별 포인트 수 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class PointListResponse {

    @Schema(description = "일", example = "13")
    private final int date;

    @Schema(description = "잔디 레벨", example = "1")
    private final int grassLevel;
}
