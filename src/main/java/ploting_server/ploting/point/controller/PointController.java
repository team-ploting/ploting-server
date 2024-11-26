package ploting_server.ploting.point.controller;

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
import ploting_server.ploting.point.dto.response.PointInfoResponse;
import ploting_server.ploting.point.dto.response.PointListResponse;
import ploting_server.ploting.point.service.PointService;

import java.util.List;

@RestController
@RequestMapping("points")
@RequiredArgsConstructor
@Tag(name = "포인트", description = "포인트 관련 API")
public class PointController {

    private final PointService pointService;

    @Operation(
            summary = "포인트 받기",
            description = "포인트를 받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "포인트 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }"))
            )
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> receivePoints(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long missionId
    ) {
        pointService.receivePoints(Long.parseLong(principalDetails.getUsername()), missionId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "날짜별 포인트 수 조회 (잔디)",
            description = "날짜별 포인트 수를 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "날짜별 포인트 수 조회 성공",
                    useReturnTypeSchema = true
            )
    })
    @GetMapping("/{memberId}/grass")
    public ResponseEntity<BfResponse<List<PointListResponse>>> receivePoints(
            @PathVariable Long memberId
    ) {
        List<PointListResponse> pointsByDate = pointService.getPointsByDate(memberId);
        return ResponseEntity.ok(new BfResponse<>(pointsByDate));
    }

    @Operation(
            summary = "레벨 및 포인트 수 조회",
            description = "레벨 및 포인트 수를 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "레벨 및 포인트 수 조회 성공",
                    useReturnTypeSchema = true
            )
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<BfResponse<PointInfoResponse>> getMyPointAndLevel(
            @PathVariable Long memberId
    ) {
        PointInfoResponse pointInfoResponse = pointService.getMyPointAndLevel(memberId);
        return ResponseEntity.ok(new BfResponse<>(pointInfoResponse));
    }
}