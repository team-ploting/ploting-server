package ploting_server.ploting.organization.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;
import ploting_server.ploting.organization.dto.response.OrganizationInfoResponse;
import ploting_server.ploting.organization.dto.response.OrganizationListResponse;
import ploting_server.ploting.organization.dto.response.OrganizationMemberListResponse;
import ploting_server.ploting.organization.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("organizations")
@RequiredArgsConstructor
@Tag(name = "단체", description = "단체 관련 API")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(
            summary = "모든 단체 목록 조회 (페이징 처리)",
            description = "페이징 처리된 모든 단체 목록을 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 단체 목록 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("")
    public ResponseEntity<BfResponse<Page<OrganizationListResponse>>> getAllOrganizations(
            @RequestParam int page,
            @RequestParam int size) {
        Page<OrganizationListResponse> allOrganizations = organizationService.getAllOrganizations(page, size);
        return ResponseEntity.ok(new BfResponse<>(allOrganizations));
    }

    @Operation(
            summary = "나의 단체 목록 조회 (페이징 처리)",
            description = "페이징 처리된 나의 단체 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 단체 목록 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("/self")
    public ResponseEntity<BfResponse<Page<OrganizationListResponse>>> getMyOrganizations(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam int page,
            @RequestParam int size) {
        Page<OrganizationListResponse> myOrganizations = organizationService.getMyOrganizations(Long.parseLong(principalDetails.getUsername()), page, size);
        return ResponseEntity.ok(new BfResponse<>(myOrganizations));
    }

    @Operation(
            summary = "단체 세부 정보 조회",
            description = "단체의 세부 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 세부 정보 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("/{organizationId}")
    public ResponseEntity<BfResponse<OrganizationInfoResponse>> getOrganizationInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId) {
        OrganizationInfoResponse organizationInfoResponse = organizationService.getOrganizationInfo(
                Long.parseLong(principalDetails.getUsername()), organizationId);
        return ResponseEntity.ok(new BfResponse<>(organizationInfoResponse));
    }

    @Operation(
            summary = "단체 멤버 리스트 조회",
            description = "단체의 멤버 리스트를 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 멤버 리스트 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("/{organizationId}/members")
    public ResponseEntity<BfResponse<List<OrganizationMemberListResponse>>> getOrganizationMemberList(
            @PathVariable Long organizationId) {
        List<OrganizationMemberListResponse> organizationMemberList = organizationService.getOrganizationMemberList(organizationId);
        return ResponseEntity.ok(new BfResponse<>(organizationMemberList));
    }

    @Operation(
            summary = "단체 가입",
            description = "단체를 가입합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 가입 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("{organizationId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> registerOrganization(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId,
            @RequestParam String introduction) {
        organizationService.registerOrganization(Long.parseLong(principalDetails.getUsername()), organizationId, introduction);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "단체 탈퇴",
            description = "단체를 탈퇴합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 탈퇴 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("{organizationId}/departure")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> departOrganization(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId) {
        organizationService.departOrganization(Long.parseLong(principalDetails.getUsername()), organizationId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
