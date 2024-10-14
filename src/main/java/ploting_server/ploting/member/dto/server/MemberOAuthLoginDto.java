package ploting_server.ploting.member.dto.server;

import lombok.Builder;
import lombok.Getter;
import ploting_server.ploting.member.entity.ProviderType;
import ploting_server.ploting.member.entity.RoleType;

/**
 * OAuth 로그인 요청 시 사용되는 회원 정보 DTO 클래스입니다.
 */
@Getter
@Builder
public class MemberOAuthLoginDto {

    private final String oauthId;
    private final ProviderType provider;
    private final String name;
    private final String profileImageUrl;
    private final RoleType role;
}
