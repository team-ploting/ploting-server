package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 모임 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum MeetingErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    INVALID_MEMBER_LIMIT(400, "최대 멤버 제한이 현재 멤버 수보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_MEETING(400, "멤버 수가 1명(모임장)이 아닐 경우 모임을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),

    // 403 FORBIDDEN
    NOT_MEETING_MEMBER(403, "모임에 가입된 멤버가 아닙니다.", HttpStatus.FORBIDDEN),
    NOT_MEETING_LEADER(403, "모임의 모임장이 아닙니다.", HttpStatus.FORBIDDEN),

    // 404 NOT_FOUND
    NOT_FOUND_MEETING_ID(404, "존재하지 않는 모임입니다.", HttpStatus.NOT_FOUND),
    ;

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    MeetingErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
