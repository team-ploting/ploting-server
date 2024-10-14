package ploting_server.ploting.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import ploting_server.ploting.member.entity.GenderType;

import java.time.LocalDate;

/**
 * 로그인 요청 시 사용되는 회원 정보 DTO 클래스입니다.
 */
@Getter
@Builder
public class MemberRegisterRequest {

    @Schema(description = "회원의 이름", example = "이소은", required = true)
    private final String name;

    @Schema(description = "회원의 닉네임", example = "twocowsilver", required = true)
    private final String nickname;

    @Schema(description = "회원의 지역", example = "동대문구", required = true)
    private final String location;

    @Schema(description = "회원의 성별", example = "FEMALE", required = true)
    private final GenderType gender;

    @Schema(description = "회원의 생년월일", example = "2003-02-12", required = true)
    private final LocalDate birth;
}
