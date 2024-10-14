package ploting_server.ploting.member.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import ploting_server.ploting.member.dto.request.MemberRegisterRequest;
import ploting_server.ploting.member.service.MemberService;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원 가입 시 추가 정보 등록",
            description = "소셜 로그인 후 회원의 추가 정보를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 추가 정보 등록 성공", useReturnTypeSchema = true),
    })
    @PatchMapping("/registration")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> registerMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody MemberRegisterRequest memberRegisterRequest) {
        memberService.registerMember(Long.parseLong(principalDetails.getUsername()), memberRegisterRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
