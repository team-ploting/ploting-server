package ploting_server.ploting.member.service;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ploting_server.ploting.core.code.error.JwtErrorCode;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.JwtException;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.security.dto.jwt.server.JwtTokenDto;
import ploting_server.ploting.core.security.service.jwt.JwtService;
import ploting_server.ploting.member.dto.server.MemberJwtDto;
import ploting_server.ploting.member.dto.server.MemberLoginDto;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;

/**
 * OAuth 인증과 JWT 토큰 생성을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    /**
     * OAuth 로그인
     */
    @Transactional
    public JwtTokenDto login(MemberLoginDto memberLoginDto) {
        // DB 회원 정보 조회
        Member member = memberRepository.findByOauthIdAndRole(memberLoginDto.getOauthId(), memberLoginDto.getRole())
                .orElseGet(() -> registerOAuthMember(memberLoginDto)); // 회원 정보가 없을 경우 회원가입

        MemberJwtDto memberJwtDto = new MemberJwtDto(member);

        // JWT 토큰 생성 및 반환
        return createAccessTokenAndRefreshToken(memberJwtDto);
    }

    /**
     * 기존 Refresh token 으로 신규 Access token 및 Refresh token 발급
     */
    @Transactional
    public JwtTokenDto refreshTokens(String refreshToken) {
        // 유효성 검증
        try {
            jwtService.validateToken(refreshToken);
        } catch (Exception e) {
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }

        // ID로 회원 조회
        Claims claims = jwtService.getClaims(refreshToken);
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
    private Member registerOAuthMember(MemberLoginDto memberLoginDto) {
        Member newMember = Member.builder()
                .oauthId(memberLoginDto.getOauthId())
                .provider(memberLoginDto.getProvider())
                .name(memberLoginDto.getName())
                .profileImageUrl(memberLoginDto.getProfileImageUrl())
                .role(memberLoginDto.getRole())
                .level(1)
                .build();

        return memberRepository.save(newMember);
    }

    /**
     * Access Token 및 Refresh Token 발급
     */
    private JwtTokenDto createAccessTokenAndRefreshToken(MemberJwtDto memberJwtDto) {
        String accessToken = jwtService.createAccessToken(memberJwtDto);
        String refreshToken = jwtService.createRefreshToken(memberJwtDto);

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
