package ploting_server.ploting.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ploting_server.ploting.member.entity.GenderType;
import ploting_server.ploting.point.entity.LevelType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class MemberInfoResponse {

    @Schema(description = "회원의 Id", example = "1")
    private final Long id;

    @Schema(description = "회원의 이름", example = "이소은")
    private final String name;

    @Schema(description = "회원의 닉네임", example = "twocowsilver")
    private final String nickname;

    @Schema(description = "회원의 지역", example = "동대문구")
    private final String location;

    @Schema(description = "회원의 레벨", example = "1")
    private final int level;

    @Schema(description = "회원의 레벨 타입", example = "씨앗")
    private final LevelType levelType;

    @Schema(description = "회원의 성별", example = "FEMALE")
    private final GenderType gender;

    @Schema(description = "회원의 생년월일", example = "2003-02-12")
    private final LocalDate birth;

    @Schema(description = "계정 생성일자", example = "2024-10-12T14:34:56")
    private final LocalDateTime createdAt;
}
