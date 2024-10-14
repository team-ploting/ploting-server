package ploting_server.ploting.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회원 닉네임 중복 여부를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class MemberNicknameDuplicationResponse {

    @Schema(description = "닉네임 중복 여부, 중복이면 true", example = "true")
    private final boolean isMemberNicknameDuplicated;
}
