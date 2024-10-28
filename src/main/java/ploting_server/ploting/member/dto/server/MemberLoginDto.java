package ploting_server.ploting.member.dto.server;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ploting_server.ploting.member.entity.ProviderType;
import ploting_server.ploting.member.entity.RoleType;

/**
 * OAuth 로그인 요청에 필요한 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class MemberLoginDto {

    private final String oauthId;
    private final ProviderType provider;
    private final String name;
    private final String profileImageUrl;
    private final RoleType role;
}
