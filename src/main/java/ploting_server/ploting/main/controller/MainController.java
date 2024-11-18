package ploting_server.ploting.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.main.dto.response.FilteredResponse;
import ploting_server.ploting.main.service.MainService;

import java.util.List;

@RestController
@RequestMapping("mains")
@RequiredArgsConstructor
@Tag(name = "메인", description = "메인 관련 API")
public class MainController {

    private final MainService mainService;

    @Operation(
            summary = "모든 게시글과 모임 조회",
            description = "모든 게시글과 모임을 생성일자 정렬로 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 게시글과 모임 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("")
    public ResponseEntity<BfResponse<List<FilteredResponse>>> getFilteredPostAndMeeting(
            @RequestParam(required = false) String search) {
        List<FilteredResponse> filteredPostAndMeeting = mainService.getFilteredPostAndMeeting(search);
        return ResponseEntity.ok(new BfResponse<>(filteredPostAndMeeting));
    }
}
