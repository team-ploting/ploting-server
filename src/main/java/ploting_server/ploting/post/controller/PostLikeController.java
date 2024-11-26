package ploting_server.ploting.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;
import ploting_server.ploting.post.service.PostLikeService;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
@Tag(name = "게시글 좋아요", description = "게시글 좋아요 관련 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(
            summary = "좋아요 누르기",
            description = "게시글의 좋아요를 누릅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("{postId}/like")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> pressPostLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long postId
    ) {
        postLikeService.pressPostLike(Long.parseLong(principalDetails.getUsername()), postId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "좋아요 취소",
            description = "게시글의 좋아요를 취소합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("{postId}/like")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> cancelPostLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long postId
    ) {
        postLikeService.cancelPostLike(Long.parseLong(principalDetails.getUsername()), postId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
