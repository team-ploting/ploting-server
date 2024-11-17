package ploting_server.ploting.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@RequiredArgsConstructor
public class MeetingListResponse {

    @Schema(description = "모임의 id", example = "1")
    private final Long id;

    @Schema(description = "모임의 이름", example = "모임의 이름")
    private final String name;

    @Schema(description = "모임 날짜", example = "2024-08-17")
    private final LocalDate meetDate;

    @Schema(description = "모임 시간", example = "18")
    private final int meetHour;

    @Schema(description = "모임의 지역", example = "모임의 지역")
    private final String location;

    @Schema(description = "최소 레벨 제한", example = "5")
    private final int minLevel;

    @Schema(description = "멤버 수", example = "15")
    private final int memberCount;

    @Schema(description = "최대 멤버 제한", example = "50")
    private final int maxMember;

    @Schema(description = "최소 나이 제한", example = "20")
    private final int minAge;

    @Schema(description = "최대 나이 제한", example = "27")
    private final int maxAge;

    @Schema(description = "남성 멤버 수", example = "7")
    private final int maleCount;

    @Schema(description = "여성 멤버 수", example = "8")
    private final int femaleCount;
}
