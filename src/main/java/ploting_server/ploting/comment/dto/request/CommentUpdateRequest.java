package ploting_server.ploting.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 댓글 수정 시 댓글 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class CommentUpdateRequest {

    @Schema(description = "댓글 본문", example = "갑사합니다", required = true)
    private final String content;
}
