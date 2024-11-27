package ploting_server.ploting.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ploting_server.ploting.comment.dto.response.CommentListResponse;
import ploting_server.ploting.point.entity.LevelType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 세부 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class PostInfoResponse {

    @Schema(description = "게시글 작성자 닉네임", example = "환경지킴이")
    private final String authorNickname;

    @Schema(description = "게시글 작성자 지역", example = "강서구")
    private final String authorLocation;

    @Schema(description = "게시글 작성자 레벨", example = "1")
    private final int authorLevel;

    @Schema(description = "게시글 작성자 레벨 타입", example = "씨앗")
    private final LevelType authorLevelType;

    @Schema(description = "게시글 제목", example = "안녕하세요 플로깅을 하고 싶은데")
    private final String title;

    @Schema(description = "게시글 본문", example = "플로깅을 해보고싶은데 플로깅 어떻게 해야하나요")
    private final String content;

    @Schema(description = "게시글 좋아요 수", example = "16")
    private final int likeCount;

    @Schema(description = "게시글 댓글 수", example = "16")
    private final int commentCount;

    @Schema(description = "좋아요 여부", example = "true")
    private final boolean hasLiked;

    @Schema(description = "게시글 작성자 여부", example = "true")
    private final boolean myPost;

    @Schema(description = "게시글 생성 시간", example = "2024-09-21T12:30:00")
    private final LocalDateTime createdAt;

    @Schema(description = "게시글의 댓글")
    private final List<CommentListResponse> comments;
}
