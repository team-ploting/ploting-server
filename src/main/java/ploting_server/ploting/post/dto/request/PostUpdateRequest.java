package ploting_server.ploting.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 게시글 수정 시 게시글 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class PostUpdateRequest {

    @Schema(description = "게시글 제목", example = "귤껍질 어디다 버리는지 아시나요", required = true)
    private final String title;

    @Schema(description = "게시글 본문", example = "귤껍질 어디에 버리는지 모르겠어요", required = true)
    private final String content;
}
