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

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    PointErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
