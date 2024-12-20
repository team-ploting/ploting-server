package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 미션 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum MissionErrorCode implements BaseErrorCode {
    // 404 NOT_FOUND
    NOT_FOUND_MISSION_ID(404, "존재하지 않는 미션입니다.", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    MissionErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
