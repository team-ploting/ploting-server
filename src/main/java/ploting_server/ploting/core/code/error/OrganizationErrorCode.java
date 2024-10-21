package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 단체 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum OrganizationErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    INVALID_MEMBER_LIMIT(400, "최대 멤버 제한이 현재 멤버 수보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_ORGANIZATION(400, "멤버 수가 1명(단체장)이 아닐 경우 단체를 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),

    // 403 FORBIDDEN
    NOT_ORGANIZATION_MEMBER(403, "단체에 가입된 멤버가 아닙니다.", HttpStatus.FORBIDDEN),
    NOT_ORGANIZATION_LEADER(403, "단체의 단체장이 아닙니다.", HttpStatus.FORBIDDEN),

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
