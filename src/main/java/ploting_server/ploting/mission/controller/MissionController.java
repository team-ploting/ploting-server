package ploting_server.ploting.mission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.mission.dto.request.MissionCreateRequest;
import ploting_server.ploting.mission.service.MissionService;

@RestController
@RequestMapping("missions")
@RequiredArgsConstructor
@Tag(name = "미션", description = "미션 관련 API")
public class MissionController {

    private final MissionService missionService;

    @Operation(
            summary = "미션 생성 (어드민 권한 필요)",
            description = "미션을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> createMission(
            @RequestBody MissionCreateRequest missionCreateRequest) {
        missionService.createMission(missionCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "미션 삭제 (어드민 권한 필요)",
            description = "미션을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deleteMission(
            @RequestParam Long missionId) {
        missionService.deleteMission(missionId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
