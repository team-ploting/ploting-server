package ploting_server.ploting.core.security.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import ploting_server.ploting.core.code.error.OAuthErrorCode;
import ploting_server.ploting.core.exception.OAuthException;
import ploting_server.ploting.core.security.dto.jwt.server.NaverErrorResponseDto;
import ploting_server.ploting.core.security.dto.oauth.NaverResponseDto;
import ploting_server.ploting.core.security.dto.oauth.NaverUserInfoDto;
import ploting_server.ploting.member.dto.request.OAuthAuthorizationRequest;
import ploting_server.ploting.member.dto.request.OAuthLoginRequest;
import ploting_server.ploting.member.dto.server.MemberOAuthLoginDto;
import ploting_server.ploting.member.entity.ProviderType;
import reactor.core.publisher.Mono;

/**
 * Naver OAuth 인증을 통해 액세스 토큰 발급 및 사용자 정보를 조회하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String baseUri;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUri;

    private final String contentType = "application/x-www-form-urlencoded;charset=utf-8";

    /**
     * OAuth 인증 코드로 액세스 토큰을 발급받는 메서드 (백엔드 테스트 용도)
     */
    @Override
    public String getAccessToken(OAuthAuthorizationRequest oAuthAuthorizationRequest) {

        WebClient webClient = WebClient.builder()
                .baseUrl(tokenUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
                .build();

        NaverResponseDto response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", oAuthAuthorizationRequest.getCode())
                        .queryParam("state", oAuthAuthorizationRequest.getState())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, responseEntity -> Mono.error(new OAuthException(OAuthErrorCode.BAD_REQUEST_OAUTH_USER_INFO)))
                .onStatus(HttpStatusCode::is5xxServerError, responseEntity -> Mono.error(new OAuthException(OAuthErrorCode.OAUTH_PROVIDER_SERVER_ERROR)))
                .bodyToMono(NaverResponseDto.class)
                .block();

        return response.getAccessToken();
    }

    /**
     * OAuth 제공자에서 사용자 정보를 가져와 MemberLoginDto로 변환
     */
    @Override
    public MemberOAuthLoginDto getUserInfo(OAuthLoginRequest oAuthLoginRequest) {

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthLoginRequest.getAccessToken())
                .build();

        NaverUserInfoDto userinfo = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> handleClientError(clientResponse))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new OAuthException(OAuthErrorCode.OAUTH_PROVIDER_SERVER_ERROR)))
                .bodyToMono(NaverUserInfoDto.class)
                .block();

        return MemberOAuthLoginDto.builder()
                .oauthId(userinfo.getResponse().getOauthId())
                .provider(ProviderType.NAVER)
                .name(userinfo.getResponse().getName())
                .profileImageUrl(userinfo.getResponse().getProfileImageUrl())
                .role(oAuthLoginRequest.getRole())
                .build();
    }

    /**
     * OAuth 클라이언트 오류 처리 메서드
     */
    private Mono<? extends Throwable> handleClientError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(NaverErrorResponseDto.class)
                .flatMap(errorResponse -> {
                    String errorCode = errorResponse.getErrorCode();
                    return switch (errorCode) {
                        case "024" -> Mono.error(new OAuthException(OAuthErrorCode.AUTHENTICATION_FAILED));
                        case "028" -> Mono.error(new OAuthException(OAuthErrorCode.OAUTH_HEADER_NOT_EXISTS));
                        case "403" -> Mono.error(new OAuthException(OAuthErrorCode.FORBIDDEN));
                        case "404" -> Mono.error(new OAuthException(OAuthErrorCode.NOT_FOUND));
                        default -> Mono.error(new OAuthException(OAuthErrorCode.BAD_REQUEST_OAUTH_USER_INFO));
                    };
                });
    }
}
