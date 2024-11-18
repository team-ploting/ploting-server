package ploting_server.ploting.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 댓글 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class CommentInfoResponse {

    @Schema(description = "댓글 작성자 닉네임", example = "환경지킴이")
    private final String authorNickname;

    @Schema(description = "댓글 작성자 지역", example = "강서구")
    private final String authorLocation;

    @Schema(description = "댓글 작성자 레벨", example = "10")
    private final int authorLevel;

    @Schema(description = "댓글 본문", example = "네 홈화면에 있는 모임글에...")
    private final String content;

    @Schema(description = "댓글 좋아요 수", example = "16")
    private final int likeCount;

    @Schema(description = "댓글 좋아요 여부", example = "true")
    private final boolean hasLiked;

    @Schema(description = "댓글 작성자 여부", example = "true")
    private final boolean myComment;
}
