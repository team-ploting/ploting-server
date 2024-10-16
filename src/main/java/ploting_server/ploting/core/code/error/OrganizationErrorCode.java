package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 단체 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum OrganizationErrorCode implements BaseErrorCode {
    // 404 NOT_FOUND
    NOT_FOUND_ORGANIZATION_ID(404, "존재하지 않는 단체입니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    OrganizationErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
