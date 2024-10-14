package ploting_server.ploting.member.controller;

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
import ploting_server.ploting.member.dto.request.MemberRegisterRequest;
import ploting_server.ploting.member.dto.request.MemberUpdateRequest;
import ploting_server.ploting.member.dto.response.MemberInfoResponse;
import ploting_server.ploting.member.dto.response.MemberNicknameDuplicationResponse;
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
            @ApiResponse(responseCode = "200", description = "회원 추가 정보 등록 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("/registration")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> registerMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody MemberRegisterRequest memberRegisterRequest) {
        memberService.registerMember(Long.parseLong(principalDetails.getUsername()), memberRegisterRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "회원 정보 조회",
            description = "회원의 정보를 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<BfResponse<MemberInfoResponse>> getMember(
            @PathVariable Long memberId) {
        MemberInfoResponse memberInfoResponse = memberService.getMember(memberId);
        return ResponseEntity.ok(new BfResponse<>(memberInfoResponse));
    }

    @Operation(
            summary = "회원 닉네임 중복 확인",
            description = "회원의 닉네임 중복 여부를 확인합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 닉네임 중복 여부 조회 성공 (중복이면 true)", useReturnTypeSchema = true),
    })
    @GetMapping("/check-nickname")
    public ResponseEntity<BfResponse<MemberNicknameDuplicationResponse>> checkMemberNickname(
            @RequestParam String nickname) {
        MemberNicknameDuplicationResponse memberNicknameDuplicationResponse = memberService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(new BfResponse<>(memberNicknameDuplicationResponse));
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원의 정보를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMember(Long.parseLong(principalDetails.getUsername()), memberUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "회원 탈퇴 (soft delete)",
            description = "회원을 탈퇴 처리합니다. (soft delete)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 처리 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deleteMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberService.deleteMember(Long.parseLong(principalDetails.getUsername()));
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
