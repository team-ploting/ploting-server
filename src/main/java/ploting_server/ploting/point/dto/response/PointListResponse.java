package ploting_server.ploting.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * 날짜별 포인트 수 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class PointListResponse {

    @Schema(description = "날짜", example = "2024-11-19")
    private final LocalDate date;

    @Schema(description = "포인트 합계", example = "20")
    private final int totalPoints;
}
