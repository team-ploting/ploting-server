package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 필터 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum FilterErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    INVALID_FILTER_DATA_TYPE(400, "필터 데이터의 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    FilterErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
