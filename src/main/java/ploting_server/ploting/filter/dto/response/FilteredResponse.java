package ploting_server.ploting.filter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import ploting_server.ploting.filter.entity.FilterType;

@Getter
@Builder
public class FilteredResponse {

    @Schema(description = "필터 타입", example = "MEETING/POST")
    private FilterType type;

    @Schema(description = "필터링된 데이터")
    private Object body;
}
