package ploting_server.ploting.meeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;
import ploting_server.ploting.meeting.dto.request.MeetingCreateRequest;
import ploting_server.ploting.meeting.service.MeetingLeaderService;

@RestController
@RequestMapping("meetings")
@RequiredArgsConstructor
@Tag(name = "모임장", description = "모임장 관련 API")
public class MeetingLeaderController {

    private final MeetingLeaderService meetingLeaderService;

    @Operation(
            summary = "모임 생성",
            description = "모임장으로서 모임을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 생성 성공", useReturnTypeSchema = true),
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> createMeeting(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long meetingId,
            @RequestBody MeetingCreateRequest meetingCreateRequest
    ) {
        meetingLeaderService.createMeeting(Long.parseLong(principalDetails.getUsername()), meetingId, meetingCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
