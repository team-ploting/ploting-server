package ploting_server.ploting.organization.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;

import java.util.List;

/**
 * 단체 세부 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class OrganizationInfoResponse {

    @Schema(description = "단체의 이름", example = "단체의 이름")
    private final String name;

    @Schema(description = "단체의 설명", example = "단체의 설명")
    private final String description;

    @Schema(description = "단체의 지역", example = "강서구")
    private final String location;

    @Schema(description = "최대 멤버 제한", example = "50")
    private final int maxMember;

    @Schema(description = "최소 나이 제한", example = "20")
    private final int minAge;

    @Schema(description = "최대 나이 제한", example = "27")
    private final int maxAge;

    @Schema(description = "최소 레벨 제한", example = "5")
    private final int minLevel;

    @Schema(description = "좋아요 수", example = "16")
    private final int likeCount;

    @Schema(description = "좋아요 여부", example = "true")
    private final boolean hasLiked;

    @Schema(description = "멤버 수", example = "30")
    private final int memberCount;

    @Schema(description = "단체장 이름", example = "환경마스터")
    private final String leaderName;

    @Schema(description = "단체장 레벨", example = "10")
    private final int leaderLevel;

    @Schema(description = "남성 멤버 수", example = "10")
    private final int maleCount;

    @Schema(description = "여성 멤버 수", example = "20")
    private final int femaleCount;

    @Schema(description = "단체의 모임 리스트")
    private final List<MeetingListResponse> meetingListResponse;
}
