package ploting_server.ploting.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class PostListResponse {

    @Schema(description = "게시글의 id", example = "1")
    private final Long id;

    @Schema(description = "게시글의 제목", example = "귤껍질 어디다 버리는지 아시나요")
    private final String title;

    @Schema(description = "게시글의 본문", example = "귤껍질은 말려서 일반쓰레기에 버리면 돼요")
    private final String content;

    @Schema(description = "게시글 작성자 닉네임", example = "환경지킴이")
    private final String authorNickName;

    @Schema(description = "좋아요 수", example = "12")
    private final int likeCount;

    @Schema(description = "댓글 수", example = "5")
    private final int commentCount;

    @Schema(description = "게시글 생성 시간", example = "2024-09-21T12:30:00")
    private final LocalDateTime createdAt;
}
