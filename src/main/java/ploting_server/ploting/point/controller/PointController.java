package ploting_server.ploting.point.controller;

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
import ploting_server.ploting.point.dto.response.PointListResponse;
import ploting_server.ploting.point.service.PointService;

import java.time.LocalDate;
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
            @ApiResponse(responseCode = "200", description = "포인트 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> receivePoints(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long missionId) {
        pointService.receivePoints(Long.parseLong(principalDetails.getUsername()), missionId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "날짜별 포인트 수 조회 (잔디)",
            description = "날짜별 포인트 수를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 수 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/grass")
    public ResponseEntity<BfResponse<List<PointListResponse>>> receivePoints(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<PointListResponse> pointsByDate = pointService.getPointsByDate(Long.parseLong(principalDetails.getUsername()), startDate, endDate);
        return ResponseEntity.ok(new BfResponse<>(pointsByDate));
    }
}
