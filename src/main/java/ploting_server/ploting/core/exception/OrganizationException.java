package ploting_server.ploting.core.exception;

import lombok.Getter;
import ploting_server.ploting.core.code.error.BaseErrorCode;

/**
 * 단체 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class OrganizationException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public OrganizationException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
