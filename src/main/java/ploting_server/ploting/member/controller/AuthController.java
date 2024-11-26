package ploting_server.ploting.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.dto.jwt.request.RefreshTokenRequest;
import ploting_server.ploting.core.security.dto.jwt.response.JwtTokenResponse;
import ploting_server.ploting.member.service.AuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "인증, 인가 및 토큰 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "토큰 갱신",
            description = "기존 Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT 토큰 갱신 성공", useReturnTypeSchema = true),
    })
    @PostMapping("/refresh")
    public ResponseEntity<BfResponse<JwtTokenResponse>> refreshTokens(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        JwtTokenResponse jwtTokenResponse = authService.refreshTokens(refreshTokenRequest);
        return ResponseEntity.ok(new BfResponse<>(jwtTokenResponse));
    }
}