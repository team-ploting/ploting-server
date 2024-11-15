package ploting_server.ploting.core.exception;

import lombok.Getter;
import ploting_server.ploting.core.code.error.BaseErrorCode;

/**
 * 게시글 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class PostException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public PostException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
