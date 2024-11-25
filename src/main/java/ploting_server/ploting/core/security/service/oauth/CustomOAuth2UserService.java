package ploting_server.ploting.core.security.service.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ploting_server.ploting.core.code.error.OAuthErrorCode;
import ploting_server.ploting.core.exception.OAuthException;
import ploting_server.ploting.core.security.dto.oauth.CustomOAuth2User;
import ploting_server.ploting.core.security.dto.oauth.KakaoUserInfo;
import ploting_server.ploting.core.security.dto.oauth.NaverUserInfo;
import ploting_server.ploting.core.security.dto.oauth.OAuth2UserInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2 사용자 정보 로딩 및 권한 설정을 위한 커스텀 서비스 클래스입니다.
 */
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Spring Security의 기본 OAuth2UserService를 사용하여 사용자 정보 로드
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 기본 권한 설정: ROLE_USER
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 사용자 속성 정보 로드
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 사용자 고유 ID 속성명 로드
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2 Provider 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, attributes);

        log.debug(oAuth2UserInfo.getName());

        return new CustomOAuth2User(
                authorities,
                attributes,
                userNameAttributeName,
                oAuth2UserInfo.getId(),
                oAuth2UserInfo.getProvider(),
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getImageUrl()
        );
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "naver" -> new NaverUserInfo(attributes);
            case "kakao" -> new KakaoUserInfo(attributes);
//            case "goggle" -> new GoogleUserInfo(attributes);
            default -> throw new OAuthException(OAuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }
}