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
    INVALID_MEMBER_LIMIT(400, "최대 멤버 수는 현재 멤버 수보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_MEETING(400, "모임을 삭제하려면 모임장 한 명만 남아 있어야 합니다.", HttpStatus.BAD_REQUEST),
    CANNOT_KICK_SELF_LEADER(400, "모임장은 스스로 강퇴될 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_LEAVE_ORGANIZATION_AS_LEADER(400, "모임장은 모임을 탈퇴할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_WITHDRAW_AS_MEETING_LEADER(400, "모임장을 맡고 있는 모임이 있어 회원 탈퇴가 불가능합니다.", HttpStatus.BAD_REQUEST),
    ALREADY_REGISTERED_MEMBER(400, "이미 해당 모임에 가입된 상태입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MEETING_LIKE(400, "좋아요를 누르지 않은 상태입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_MEETING_LIKE(400, "이미 좋아요를 누른 상태입니다.", HttpStatus.BAD_REQUEST),
    AGE_NOT_ELIGIBLE(400, "해당 모임에 가입할 수 있는 나이 조건이 아닙니다.", HttpStatus.BAD_REQUEST),
    LEVEL_NOT_ELIGIBLE(400, "해당 모임에 가입할 수 있는 레벨 조건이 아닙니다.", HttpStatus.BAD_REQUEST),
    FULL_MEMBER_CAPACITY(400, "모임의 인원이 이미 꽉 찼습니다.", HttpStatus.BAD_REQUEST),

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
