package ploting_server.ploting.organization.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 단체의 멤버 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class OrganizationMemberListResponse {

    @Schema(description = "멤버의 닉네임", example = "멤버의 닉네임")
    private final String nickname;

    @Schema(description = "멤버의 레벨", example = "1")
    private final int level;

    @Schema(description = "멤버의 프로필 이미지", example = "https://...")
    private final String profileImageUrl;

    @Schema(description = "멤버의 한 줄 소개", example = "한 줄 소개")
    private final String introduction;

    @Schema(description = "단체장 여부", example = "true")
    private final boolean leaderStatus;
}
