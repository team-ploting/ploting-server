package ploting_server.ploting.core.exception;

import lombok.Getter;
import ploting_server.ploting.core.code.error.BaseErrorCode;

/**
 * 포인트 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class PointException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public PointException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
