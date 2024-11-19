package ploting_server.ploting.core.exception;

import lombok.Getter;
import ploting_server.ploting.core.code.error.BaseErrorCode;

/**
 * 필터 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class FilterException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public FilterException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
