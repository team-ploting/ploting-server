package ploting_server.ploting.core.code.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ploting_server.ploting.core.response.ErrorResponse;

/**
 * 회원 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum MemberErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    DUPLICATE_NICKNAME(400, "이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST),

    // 404 NOT_FOUND
    NOT_FOUND_MEMBER_ID(404, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MEMBER_ROLES(404, "회원 권한 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    MemberErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
