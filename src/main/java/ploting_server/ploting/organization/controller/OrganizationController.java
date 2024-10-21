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
import ploting_server.ploting.organization.dto.request.OrganizationCreateRequest;
import ploting_server.ploting.organization.dto.request.OrganizationUpdateRequest;
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
            summary = "단체 생성",
            description = "리더로서 단체를 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> registerMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody OrganizationCreateRequest organizationCreateRequest) {
        organizationService.createOrganization(Long.parseLong(principalDetails.getUsername()), organizationCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "모든 단체 목록 조회 (페이징 처리)",
            description = "페이징 처리된 모든 단체 목록을 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 단체 목록 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("")
    public ResponseEntity<BfResponse<Page<OrganizationListResponse>>> getAllOrganizationList(
            @RequestParam int page,
            @RequestParam int size) {
        Page<OrganizationListResponse> allOrganizationList = organizationService.getAllOrganizationList(page, size);
        return ResponseEntity.ok(new BfResponse<>(allOrganizationList));
    }

    @Operation(
            summary = "나의 단체 목록 조회 (페이징 처리)",
            description = "페이징 처리된 나의 단체 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 단체 목록 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("/self")
    public ResponseEntity<BfResponse<Page<OrganizationListResponse>>> getMyOrganizationList(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam int page,
            @RequestParam int size) {
        Page<OrganizationListResponse> myOrganizationList = organizationService.getMyOrganizationList(Long.parseLong(principalDetails.getUsername()), page, size);
        return ResponseEntity.ok(new BfResponse<>(myOrganizationList));
    }

    @Operation(
            summary = "단체 세부 정보 조회",
            description = "단체의 세부 정보를 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 세부 정보 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("/{organizationId}")
    public ResponseEntity<BfResponse<OrganizationInfoResponse>> getOrganizationInfo(
            @PathVariable Long organizationId) {
        OrganizationInfoResponse organizationInfoResponse = organizationService.getOrganizationInfo(organizationId);
        return ResponseEntity.ok(new BfResponse<>(organizationInfoResponse));
    }

    @Operation(
            summary = "단체 정보 수정 (단체장만 가능)",
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
        organizationService.updateOrganization(Long.parseLong(principalDetails.getUsername()), organizationId, organizationUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "단체 삭제 (단체장만 가능)",
            description = "단체를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체 정보 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deleteOrganization(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId) {
        organizationService.deleteOrganization(Long.parseLong(principalDetails.getUsername()), organizationId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
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
            summary = "단체장 권한 위임 (단체장만 가능)",
            description = "단체장 권한을 다른 회원에게 위임합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단체장 권한 위임 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("/{organizationId}/leader")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> delegateLeader(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId,
            @RequestParam Long newLeaderId) {
        organizationService.delegateLeader(Long.parseLong(principalDetails.getUsername()), organizationId, newLeaderId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "멤버 강퇴 (단체장만 가능)",
            description = "단체에서 멤버를 강퇴합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 강퇴 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("{organizationId}/banishment")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> banishMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long organizationId,
            @RequestParam Long kickMemberId) {
        organizationService.banishMember(Long.parseLong(principalDetails.getUsername()), organizationId, kickMemberId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
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
