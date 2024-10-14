package ploting_server.ploting.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원 정보를 업데이트하기 위한 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
public class MemberUpdateRequest {

    @Schema(description = "회원의 이름", example = "이소은", required = true)
    private final String name;

    @Schema(description = "회원의 닉네임", example = "twocowsilver", required = true)
    private final String nickname;

    @Schema(description = "회원의 지역", example = "동대문구", required = true)
    private final String location;
}
