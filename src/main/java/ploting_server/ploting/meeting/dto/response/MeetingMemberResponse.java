package ploting_server.ploting.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 단체의 멤버 응답을 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class MeetingMemberResponse {

    @Schema(description = "모임의 멤버 수", example = "50")
    private final int memberCount;

    @Schema(description = "모임의 멤버 리스트")
    private final List<MeetingMemberListResponse> members;
}
