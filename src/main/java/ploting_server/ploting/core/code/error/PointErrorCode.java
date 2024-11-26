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
    INVALID_LEVEL(400, "유효하지 않은 레벨입니다.", HttpStatus.BAD_REQUEST),
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
