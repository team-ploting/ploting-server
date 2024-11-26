package ploting_server.ploting.core.exception;

import lombok.Getter;
import ploting_server.ploting.core.code.error.BaseErrorCode;

/**
 * 인증 권한이 없는 엔드포인트 접근 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class CustomSecurityException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CustomSecurityException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
