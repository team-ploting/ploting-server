package ploting_server.ploting.member.service;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ploting_server.ploting.core.code.error.JwtErrorCode;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.OAuthErrorCode;
import ploting_server.ploting.core.exception.JwtException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.OAuthException;
import ploting_server.ploting.core.security.dto.jwt.request.RefreshTokenRequest;
import ploting_server.ploting.core.security.dto.jwt.response.AccessTokenResponse;
import ploting_server.ploting.core.security.dto.jwt.response.JwtTokenResponse;
import ploting_server.ploting.core.security.service.jwt.JwtService;
import ploting_server.ploting.core.security.service.oauth.KakaoOAuthService;
import ploting_server.ploting.core.security.service.oauth.NaverOAuthService;
import ploting_server.ploting.core.security.service.oauth.OAuthService;
import ploting_server.ploting.member.dto.request.OAuthAuthorizationRequest;
import ploting_server.ploting.member.dto.request.OAuthLoginRequest;
import ploting_server.ploting.member.dto.server.MemberJwtDto;
import ploting_server.ploting.member.dto.server.MemberOAuthLoginDto;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.entity.ProviderType;
import ploting_server.ploting.member.repository.MemberRepository;

/**
 * OAuth 인증과 JWT 토큰 생성을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final KakaoOAuthService kakaoOAuthService;
    private final NaverOAuthService naverOAuthService;

    /**
     * Provider 서버에서 AccessToken 발급
     * (백엔드 테스트 용도)
     */
    public AccessTokenResponse getAccessToken(OAuthAuthorizationRequest oAuthAuthorizationRequest) {
        OAuthService oAuthService = getOAuthService(oAuthAuthorizationRequest.getProvider());
        String accessToken = oAuthService.getAccessToken(oAuthAuthorizationRequest);

        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * OAuth 로그인
     */
    @Transactional
    public JwtTokenResponse login(OAuthLoginRequest oAuthLoginRequest) {
        OAuthService oAuthService = getOAuthService(oAuthLoginRequest.getProvider());

        // OAuth 제공자로부터 회원 정보 조회
        MemberOAuthLoginDto memberOAuthLoginDto = oAuthService.getUserInfo(oAuthLoginRequest);

        // DB 회원 정보 조회
        Member member = memberRepository.findByOauthIdAndRole(memberOAuthLoginDto.getOauthId(), memberOAuthLoginDto.getRole())
                .orElseGet(() -> registerOAuthMember(memberOAuthLoginDto)); // 회원 정보가 없을 경우 회원가입

        MemberJwtDto memberJwtDto = new MemberJwtDto(member);

        // JWT 토큰 생성 및 반환
        return createAccessTokenAndRefreshToken(memberJwtDto);
    }

    /**
     * 기존 Refresh token 으로 신규 Access token 및 Refresh token 발급
     */
    @Transactional
    public JwtTokenResponse refreshTokens(RefreshTokenRequest refreshTokenRequest) {
        // 유효성 검증
        try {
            jwtService.validateToken(refreshTokenRequest.getRefreshToken());
        } catch (Exception e) {
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }

        // ID로 회원 조회
        Claims claims = jwtService.getClaims(refreshTokenRequest.getRefreshToken());
        Long memberId = Long.valueOf(claims.getSubject());

        // DB 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // MemberJwtDto 변환
        MemberJwtDto memberJwtDto = new MemberJwtDto(member);

        return createAccessTokenAndRefreshToken(memberJwtDto);
    }

    /**
     * OAuth 신규 회원 저장
     */
    private Member registerOAuthMember(MemberOAuthLoginDto memberOAuthLoginDto) {
        Member newMember = Member.builder()
                .oauthId(memberOAuthLoginDto.getOauthId())
                .provider(memberOAuthLoginDto.getProvider())
                .name(memberOAuthLoginDto.getName())
                .profileImageUrl(memberOAuthLoginDto.getProfileImageUrl())
                .role(memberOAuthLoginDto.getRole())
                .build();

        return memberRepository.save(newMember);
    }

    /**
     * Access Token 및 Refresh Token 발급
     */
    private JwtTokenResponse createAccessTokenAndRefreshToken(MemberJwtDto memberJwtDto) {
        String accessToken = jwtService.createAccessToken(memberJwtDto);
        String refreshToken = jwtService.createRefreshToken(memberJwtDto);

        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * OAuth 제공자 서비스 반환
     */
    private OAuthService getOAuthService(ProviderType provider) {
        return switch (provider) {
            case NAVER -> naverOAuthService;
            case KAKAO -> kakaoOAuthService;
//            case GOOGLE -> googleOAuthService;
            default -> throw new OAuthException(OAuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }
}
