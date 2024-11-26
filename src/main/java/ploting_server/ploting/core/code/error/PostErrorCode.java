package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 게시글 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum PostErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    ALREADY_EXIST_POST_LIKE(400, "이미 좋아요를 누른 상태입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_POST_LIKE(400, "좋아요를 누르지 않은 상태입니다.", HttpStatus.BAD_REQUEST),

    // 403 FORBIDDEN
    NOT_POST_AUTHOR(403, "게시글의 작성자가 아닙니다.", HttpStatus.FORBIDDEN),

    // 404 NOT_FOUND
    NOT_FOUND_POST_ID(404, "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    PostErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
