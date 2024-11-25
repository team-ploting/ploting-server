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
import ploting_server.ploting.organization.dto.request.OrganizationCreateRequest;
import ploting_server.ploting.organization.dto.request.OrganizationUpdateRequest;
import ploting_server.ploting.organization.service.OrganizationLeaderService;

@RestController
@RequestMapping("organizations")
@RequiredArgsConstructor
@Tag(name = "단체장", description = "단체장 관련 API")
public class OrganizationLeaderController {

    private final OrganizationLeaderService organizationLeaderService;

    @Operation(
            summary = "단체 생성",
            description = "단체장으로서 단체를 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> createOrganization(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody OrganizationCreateRequest organizationCreateRequest) {
        organizationLeaderService.createOrganization(Long.parseLong(principalDetails.getUsername()), organizationCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "단체 정보 수정",
            description = "단체의 정보를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 정보 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("/{organizationId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateOrganization(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId,
            @RequestBody OrganizationUpdateRequest organizationUpdateRequest) {
        organizationLeaderService.updateOrganization(Long.parseLong(principalDetails.getUsername()), organizationId, organizationUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "단체 삭제",
            description = "단체를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deleteOrganization(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId) {
        organizationLeaderService.deleteOrganization(Long.parseLong(principalDetails.getUsername()), organizationId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

//    @Operation(
//            summary = "단체장 권한 위임",
//            description = "단체장 권한을 다른 회원에게 위임합니다."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "단체장 권한 위임 성공",
//                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
//    })
//    @PatchMapping("/{organizationId}/leader")
//    public ResponseEntity<BfResponse<GlobalSuccessCode>> delegateLeader(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
//            @PathVariable Long organizationId,
//            @RequestParam Long newLeaderId) {
//        organizationLeaderService.delegateLeader(Long.parseLong(principalDetails.getUsername()), organizationId, newLeaderId);
//        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
//    }
//
//    @Operation(
//            summary = "멤버 강퇴",
//            description = "단체에서 멤버를 강퇴합니다."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "멤버 강퇴 성공",
//                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
//    })
//    @DeleteMapping("{organizationId}/banishment")
//    public ResponseEntity<BfResponse<GlobalSuccessCode>> banishMember(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
//            @PathVariable Long organizationId,
//            @RequestParam Long kickMemberId) {
//        organizationLeaderService.banishMember(Long.parseLong(principalDetails.getUsername()), organizationId, kickMemberId);
//        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
//    }
}
