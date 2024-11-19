package ploting_server.ploting.filter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;
import ploting_server.ploting.filter.dto.response.FilteredResponse;
import ploting_server.ploting.filter.service.FilterService;

import java.util.List;

@RestController
@RequestMapping("filters")
@RequiredArgsConstructor
@Tag(name = "필터", description = "필터 관련 API")
public class FilterController {

    private final FilterService filterService;

    @Operation(
            summary = "모든 게시글과 모임 조회",
            description = "모든 게시글과 모임을 생성일자 정렬로 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 게시글과 모임 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/main")
    public ResponseEntity<BfResponse<List<FilteredResponse>>> getMainItems(
            @RequestParam(required = false) String search) {
        List<FilteredResponse> mainItems = filterService.getMainItems(search);
        return ResponseEntity.ok(new BfResponse<>(mainItems));
    }

    @Operation(
            summary = "좋아요한 단체, 모임, 게시글 조회",
            description = "좋아요한 단체, 모임, 게시글을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요한 단체, 모임, 게시글 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/like")
    public ResponseEntity<BfResponse<List<FilteredResponse>>> getLikedItems(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<FilteredResponse> likedItems = filterService.getLikedItems(Long.parseLong(principalDetails.getUsername()));
        return ResponseEntity.ok(new BfResponse<>(likedItems));
    }
}
