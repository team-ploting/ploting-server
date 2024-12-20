package ploting_server.ploting.core.code.error;

import ploting_server.ploting.core.response.ErrorResponse;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션 전역적으로 사용될 에러 코드의 공통 인터페이스입니다.
 */
public interface BaseErrorCode {
    int getCode();

    String getMessage();

    HttpStatus getStatus();

    ErrorResponse getErrorResponse();
}
