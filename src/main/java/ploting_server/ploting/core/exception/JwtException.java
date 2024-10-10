package ploting_server.ploting.core.exception;

import ploting_server.ploting.core.code.error.BaseErrorCode;
import lombok.Getter;

/**
 * JWT 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class JwtException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public JwtException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
