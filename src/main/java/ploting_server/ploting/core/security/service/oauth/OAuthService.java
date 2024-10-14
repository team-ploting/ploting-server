package ploting_server.ploting.core.security.service.oauth;

import ploting_server.ploting.member.dto.request.OAuthAuthorizationRequest;
import ploting_server.ploting.member.dto.request.OAuthLoginRequest;
import ploting_server.ploting.member.dto.server.MemberOAuthLoginDto;

/**
 * OAuth 인증 서비스 인터페이스입니다.
 */
public interface OAuthService {

    String getAccessToken(OAuthAuthorizationRequest oAuthAuthorizationRequest);

    MemberOAuthLoginDto getUserInfo(OAuthLoginRequest oAuthLoginRequest);
}
