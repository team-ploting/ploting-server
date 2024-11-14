package ploting_server.ploting.meeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.service.MeetingService;

@RestController
@RequestMapping("meetings")
@RequiredArgsConstructor
@Tag(name = "모임", description = "모임 관련 API")
public class MeetingController {

    private final MeetingService meetingService;

    @Operation(
            summary = "모든 모임 목록 조회 (페이징 처리)",
            description = "페이징 처리된 모든 모임 목록을 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 모임 목록 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("")
    public ResponseEntity<BfResponse<Page<MeetingListResponse>>> getAllMeetings(
            @RequestParam int page,
            @RequestParam int size) {
        Page<MeetingListResponse> allMeetings = meetingService.getAllMeetings(page, size);
        return ResponseEntity.ok(new BfResponse<>(allMeetings));
    }
}
