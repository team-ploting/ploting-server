package ploting_server.ploting.core.exception;

import ploting_server.ploting.core.code.error.BaseErrorCode;
import ploting_server.ploting.core.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 핸들러 클래스입니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 관련 예외 Handler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(" / "));

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Jwt 관련 예외 Handler
     */
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(JwtException exception) {
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * OAuth 관련 예외 Handler
     */
    @ExceptionHandler(OAuthException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(OAuthException exception) {
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Member 관련 예외 Handler
     */
    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(MemberException exception) {
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }
}
