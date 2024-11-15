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
    DUPLICATE_NICKNAME(400, "중복된 닉네임입니다.", HttpStatus.BAD_REQUEST),

    // 404 NOT_FOUND
    NOT_FOUND_MEMBER_ID(404, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MEMBER_ROLES(404, "회원의 권한이 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    MemberErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
