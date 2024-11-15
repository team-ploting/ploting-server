package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 게시글 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum PostErrorCode implements BaseErrorCode {
    // 403 FORBIDDEN
    NOT_POST_AUTHOR(403, "게시글의 작성자가 아닙니다", HttpStatus.FORBIDDEN),

    // 404 NOT_FOUND
    NOT_FOUND_POST_ID(404, "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    ;

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    PostErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}