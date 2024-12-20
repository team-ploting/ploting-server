package ploting_server.ploting.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 프로젝트 전역적으로 사용되는 REST API 에러 응답 클래스입니다.
 */
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "400")
    private final int code;

    @Schema(description = "에러 메시지", example = "실패")
    private final String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
