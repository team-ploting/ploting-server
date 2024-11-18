package ploting_server.ploting.meeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import ploting_server.ploting.meeting.dto.request.MeetingUpdateRequest;
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
            @ApiResponse(responseCode = "200", description = "모임 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> createMeeting(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long organizationId,
            @RequestBody MeetingCreateRequest meetingCreateRequest
    ) {
        meetingLeaderService.createMeeting(Long.parseLong(principalDetails.getUsername()), organizationId, meetingCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "모임 정보 수정",
            description = "모임의 정보를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 정보 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("/{meetingId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateMeeting(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long meetingId,
            @RequestBody MeetingUpdateRequest meetingUpdateRequest) {
        meetingLeaderService.updateMeeting(Long.parseLong(principalDetails.getUsername()), meetingId, meetingUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "모임 삭제",
            description = "모임을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deleteMeeting(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long meetingId) {
        meetingLeaderService.deleteMeeting(Long.parseLong(principalDetails.getUsername()), meetingId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "멤버 강퇴",
            description = "모임에서 멤버를 강퇴합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 강퇴 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("{meetingId}/banishment")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> banishMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long meetingId,
            @RequestParam Long kickMemberId) {
        meetingLeaderService.banishMember(Long.parseLong(principalDetails.getUsername()), meetingId, kickMemberId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
