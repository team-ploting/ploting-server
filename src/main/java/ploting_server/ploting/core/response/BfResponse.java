package ploting_server.ploting.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;

/**
 * 프로젝트 전역적으로 사용되는 REST API 응답 클래스입니다.
 */
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BfResponse<T> {

    @Schema(description = "응답 코드", example = "200")
    private int code;

    @Schema(description = "응답 메시지", example = "정상 처리되었습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public BfResponse(T data) {
        this.code = GlobalSuccessCode.SUCCESS.getCode();
        this.message = GlobalSuccessCode.SUCCESS.getMessage();
        this.data = data;
    }

    public BfResponse(GlobalSuccessCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    public BfResponse(GlobalSuccessCode statusCode) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }
}
