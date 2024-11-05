package ploting_server.ploting.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 단체 수정 시 단체 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class OrganizationUpdateRequest {

    @Schema(description = "단체의 이름", example = "환경지킴이 단체", required = true)
    private final String name;

    @Schema(description = "단체의 설명", example = "모임에 대한 설명", required = true)
    private final String description;

    @Schema(description = "단체의 지역", example = "강서구", required = true)
    private final String location;

    @Schema(description = "최대 멤버", example = "50", required = true)
    private final int maxMember;

    @Schema(description = "최소 나이 제한", example = "20", required = true)
    private final int minAge;

    @Schema(description = "최대 나이 제한", example = "27", required = true)
    private final int maxAge;

    @Schema(description = "최소 레벨 제한", example = "5", required = true)
    private final int minLevel;
}
