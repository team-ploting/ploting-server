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
    CANNOT_KICK_SELF_LEADER(400, "모임장은 스스로 강퇴될 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_REGISTERED_MEMBER(400, "해당 모임에 이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST),
    CANNOT_LEAVE_ORGANIZATION_AS_LEADER(400, "모임장은 탈퇴할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MEETING_LIKE(400, "좋아요를 누르지 않은 상태입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_MEETING_LIKE(400, "이미 좋아요를 누른 상태입니다.", HttpStatus.BAD_REQUEST),
    CANNOT_WITHDRAW_AS_MEETING_LEADER(400, "회원이 모임장인 모임이 있어 탈퇴할 수 없습니다.", HttpStatus.BAD_REQUEST),

    // 403 FORBIDDEN
    NOT_MEETING_MEMBER(403, "모임에 가입된 멤버가 아닙니다.", HttpStatus.FORBIDDEN),
    NOT_MEETING_LEADER(403, "모임의 모임장이 아닙니다.", HttpStatus.FORBIDDEN),

    // 404 NOT_FOUND
    NOT_FOUND_MEETING_ID(404, "존재하지 않는 모임입니다.", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    MeetingErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
