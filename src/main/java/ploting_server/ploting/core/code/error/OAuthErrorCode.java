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

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    OAuthErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
