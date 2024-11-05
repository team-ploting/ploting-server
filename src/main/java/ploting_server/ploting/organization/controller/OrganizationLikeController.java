package ploting_server.ploting.organization.controller;

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
import ploting_server.ploting.organization.service.OrganizationLikeService;

@RestController
@RequestMapping("organizations")
@RequiredArgsConstructor
@Tag(name = "단체 좋아요", description = "단체 좋아요 관련 API")
public class OrganizationLikeController {

    private final OrganizationLikeService organizationLikeService;

    @Operation(
            summary = "좋아요 누르기",
            description = "단체의 좋아요를 누릅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 좋아요 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("{organizationId}/like")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> pressOrganizationLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId) {
        organizationLikeService.pressOrganizationLike(Long.parseLong(principalDetails.getUsername()), organizationId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "좋아요 취소",
            description = "단체의 좋아요를 취소합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 좋아요 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("{organizationId}/like")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> cancelOrganizationLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId) {
        organizationLikeService.cancelOrganizationLike(Long.parseLong(principalDetails.getUsername()), organizationId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
