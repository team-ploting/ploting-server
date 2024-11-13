package ploting_server.ploting.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ploting_server.ploting.core.code.error.BaseErrorCode;
import ploting_server.ploting.core.response.ErrorResponse;

import java.util.stream.Collectors;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 핸들러 클래스입니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 관련 예외 Handler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        log.error(">>>>> ValidationException : {}", exception);
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
    protected ResponseEntity<ErrorResponse> handleJwtException(JwtException exception) {
        log.error(">>>>> JwtException : {}", exception);
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * OAuth 관련 예외 Handler
     */
    @ExceptionHandler(OAuthException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(OAuthException exception) {
        log.error(">>>>> OAuthException : {}", exception);
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Security Access Denied 관련 예외 Handler
     */
    @ExceptionHandler(CustomSecurityException.class)
    protected ResponseEntity<ErrorResponse> handleCustomSecurityException(CustomSecurityException exception) {
        log.error(">>>>> CustomSecurityException : {}", exception);
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Member 관련 예외 Handler
     */
    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(MemberException exception) {
        log.error(">>>>> MemberException : {}", exception);
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Organization 관련 예외 Handler
     */
    @ExceptionHandler(OrganizationException.class)
    protected ResponseEntity<ErrorResponse> handleOrganizationException(OrganizationException exception) {
        log.error(">>>>> OrganizationException : {}", exception);
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }
}
