package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 포인트 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum PointErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    INVALID_LEVEL_TYPE(400, "레벨 타입 정보가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_GRASS_TYPE(400, "잔디 타입 정보가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    PointErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
