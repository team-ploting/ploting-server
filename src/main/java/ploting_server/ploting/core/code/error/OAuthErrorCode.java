package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * OAuth 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum OAuthErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    UNSUPPORTED_OAUTH_PROVIDER(400, "지원하지 않는 OAuth Provider 입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    OAuthErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
