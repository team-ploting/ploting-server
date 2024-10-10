package ploting_server.ploting.core.security.dto.jwt.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverErrorResponseDto {

    @JsonProperty("resultcode")
    private String errorCode;

    @JsonProperty("message")
    private String errorMessage;
}
