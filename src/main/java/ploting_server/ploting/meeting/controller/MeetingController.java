package ploting_server.ploting.meeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;
import ploting_server.ploting.meeting.dto.response.MeetingInfoResponse;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.dto.response.MeetingMemberResponse;
import ploting_server.ploting.meeting.service.MeetingService;

import java.util.List;

@RestController
@RequestMapping("meetings")
@RequiredArgsConstructor
@Tag(name = "모임", description = "모임 관련 API")
public class MeetingController {

    private final MeetingService meetingService;

    @Operation(
            summary = "모든 모임 목록 조회",
            description = "모든 모임 목록을 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모든 모임 목록 조회 성공",
                    useReturnTypeSchema = true
            ),
    })
    @GetMapping("")
    public ResponseEntity<BfResponse<List<MeetingListResponse>>> getAllMeetings() {
        List<MeetingListResponse> allMeetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(new BfResponse<>(allMeetings));
    }

    @Operation(
            summary = "나의 모임 목록 조회",
            description = "나의 모임 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모든 모임 목록 조회 성공",
                    useReturnTypeSchema = true
            ),
    })
    @GetMapping("/self")
    public ResponseEntity<BfResponse<List<MeetingListResponse>>> getAllMeetings(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<MeetingListResponse> allMeetings = meetingService.getMyMeetings(Long.parseLong(principalDetails.getUsername()));
        return ResponseEntity.ok(new BfResponse<>(allMeetings));
    }

    @Operation(
            summary = "모임 세부 정보 조회",
            description = "모임의 세부 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 세부 정보 조회 성공",
                    useReturnTypeSchema = true
            ),
    })
    @GetMapping("/{meetingId}")
    public ResponseEntity<BfResponse<MeetingInfoResponse>> getMeetingInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long meetingId
    ) {
        MeetingInfoResponse meetingInfoResponse = meetingService.getMeetingInfo(
                Long.parseLong(principalDetails.getUsername()), meetingId);
        return ResponseEntity.ok(new BfResponse<>(meetingInfoResponse));
    }

    @Operation(
            summary = "모임 멤버 리스트 조회",
            description = "모임의 멤버 리스트를 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 멤버 리스트 조회 성공",
                    useReturnTypeSchema = true
            ),
    })
    @GetMapping("/{meetingId}/members")
    public ResponseEntity<BfResponse<MeetingMemberResponse>> getMeetingMembers(
            @PathVariable Long meetingId
    ) {
        MeetingMemberResponse meetingMembers = meetingService.getMeetingMembers(meetingId);
        return ResponseEntity.ok(new BfResponse<>(meetingMembers));
    }

    @Operation(
            summary = "모임 가입",
            description = "모임을 가입합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 가입 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 가입한 모임",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 400, \"message\": \"이미 해당 모임에 가입된 상태입니다.\" }"))
            )
    })
    @PostMapping("{meetingId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> registerMeeting(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long meetingId
    ) {
        meetingService.registerMeeting(Long.parseLong(principalDetails.getUsername()), meetingId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "모임 탈퇴",
            description = "모임을 탈퇴합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 탈퇴 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "모임장은 탈퇴 불가",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 400, \"message\": \"모임장은 모임을 탈퇴할 수 없습니다.\" }"))
            )
    })
    @DeleteMapping("{meetingId}/departure")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> departMeeting(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long meetingId
    ) {
        meetingService.departMeeting(Long.parseLong(principalDetails.getUsername()), meetingId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
