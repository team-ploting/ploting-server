package ploting_server.ploting.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ploting_server.ploting.meeting.entity.MeetingMember;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 모임 세부 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class MeetingInfoResponse {

    @Schema(description = "단체의 이름", example = "단체의 이름")
    private final String name;

    @Schema(description = "단체의 지역", example = "강서구")
    private final String location;

    @Schema(description = "단체의 설명", example = "단체의 설명")
    private final String description;

    @Schema(description = "최대 멤버 제한", example = "50")
    private final int maxMember;

    @Schema(description = "최소 나이 제한", example = "20")
    private final int minAge;

    @Schema(description = "최대 나이 제한", example = "27")
    private final int maxAge;

    @Schema(description = "최소 레벨 제한", example = "5")
    private final int minLevel;

    @Schema(description = "만나는 날짜", example = "2024-08-17T11:00:00")
    private final LocalDateTime meetDate;

    @Schema(description = "좋아요 수", example = "16")
    private final int likeCount;

    @Schema(description = "좋아요 여부", example = "true")
    private final boolean hasLiked;

    @Schema(description = "멤버 수", example = "30")
    private final int memberCount;

    @Schema(description = "남성 멤버 수", example = "10")
    private final int maleCount;

    @Schema(description = "여성 멤버 수", example = "20")
    private final int femaleCount;

    @Schema(description = "활성화 여부", example = "true")
    private final boolean activeStatus;

    @Schema(description = "가입 순으로 멤버 세명")
    private final List<MeetingMember> top3Members;

    @Schema(description = "단체 이름", example = "환경지킴이 단체")
    private final String organizationName;

    @Schema(description = "단체 멤버 수", example = "50")
    private final int organizationMemberCount;
}