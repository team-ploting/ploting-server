package ploting_server.ploting.core.exception;

import lombok.Getter;
import ploting_server.ploting.core.code.error.BaseErrorCode;

/**
 * 댓글 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class CommentException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CommentException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
