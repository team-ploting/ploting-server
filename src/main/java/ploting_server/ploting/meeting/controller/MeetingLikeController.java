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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;
import ploting_server.ploting.meeting.service.MeetingLikeService;

@RestController
@RequestMapping("meetings")
@RequiredArgsConstructor
@Tag(name = "모임 좋아요", description = "모임 좋아요 관련 API")
public class MeetingLikeController {

    private final MeetingLikeService meetingLikeService;

    @Operation(
            summary = "좋아요 누르기",
            description = "모임의 좋아요를 누릅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 좋아요 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("{meetingId}/like")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> pressMeetingLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long meetingId) {
        meetingLikeService.pressMeetingLike(Long.parseLong(principalDetails.getUsername()), meetingId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
