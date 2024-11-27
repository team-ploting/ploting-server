package ploting_server.ploting.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 단체 가입 시 가입 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class OrganizationJoinRequest {

    @Schema(description = "가입 한줄 소개", example = "안녕하세요", required = true)
    private final String introduction;
}
