package ploting_server.ploting.organization.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 단체 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class OrganizationListResponse {

    @Schema(description = "단체의 id", example = "1")
    private final Long id;

    @Schema(description = "단체의 지역", example = "강서구")
    private final String location;

    @Schema(description = "단체의 이름", example = "단체의 이름")
    private final String name;

    @Schema(description = "단체의 설명", example = "단체의 설명")
    private final String description;

    @Schema(description = "최소 레벨 제한", example = "5")
    private final int minLevel;

    @Schema(description = "멤버 수", example = "30")
    private final int memberCount;

    @Schema(description = "최대 멤버 제한", example = "50")
    private final int maxMember;

    @Schema(description = "최소 나이 제한", example = "20")
    private final int minAge;

    @Schema(description = "최대 나이 제한", example = "27")
    private final int maxAge;

    @Schema(description = "남성 멤버 수", example = "10")
    private final int maleCount;

    @Schema(description = "여성 멤버 수", example = "20")
    private final int femaleCount;
}
