package ploting_server.ploting.core.security.service.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.security.dto.jwt.response.JwtTokenResponse;
import ploting_server.ploting.core.security.dto.oauth.CustomOAuth2User;
import ploting_server.ploting.member.dto.server.MemberLoginDto;
import ploting_server.ploting.member.entity.ProviderType;
import ploting_server.ploting.member.entity.RoleType;
import ploting_server.ploting.member.service.AuthService;

import java.io.IOException;

/**
 * CustomOAuth2UserService 인증 성공 시 처리하는 핸들러 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Value("${jwt.access-expiration-seconds}")
    private long accessExpirationSeconds;

    /**
     * OAuth2 회원가입 및 로그인
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 권한 정보 가져오기
        GrantedAuthority authority = customOAuth2User.getAuthorities().stream().findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ROLES));
        RoleType role = RoleType.valueOf(authority.getAuthority());

        // CustomOAuth2User를 MemberLoginDto로 변환
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .oauthId(customOAuth2User.getOauthId())
                .provider(ProviderType.valueOf(customOAuth2User.getProvider()))
                .name(customOAuth2User.getName())
                .profileImageUrl(customOAuth2User.getProfileImageUrl())
                .role(role)
                .build();

        // 로그인 처리 및 JWT 토큰 생성
        JwtTokenResponse jwtTokens = authService.login(memberLoginDto);

        // Access Token 쿠키 설정
        setTokenCookie(response, "accessToken", jwtTokens.getAccessToken(), (int) (accessExpirationSeconds / 1000)); // 1시간 유효기간

        // 성공 후 리다이렉트
        response.sendRedirect("http://localhost:5173");
    }

    private void setTokenCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // JavaScript 접근 불가
//        cookie.setSecure(true); // HTTPS에서만 전송
        cookie.setPath("/"); // 애플리케이션 전체에서 사용 가능
        cookie.setMaxAge(maxAge); // 쿠키 유효기간 설정
        response.addCookie(cookie);
    }
}