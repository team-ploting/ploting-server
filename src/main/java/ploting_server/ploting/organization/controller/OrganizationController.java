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
import ploting_server.ploting.organization.service.OrganizationService;

@RestController
@RequestMapping("organization")
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
}
