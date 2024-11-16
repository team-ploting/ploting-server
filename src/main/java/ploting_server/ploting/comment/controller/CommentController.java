package ploting_server.ploting.comment.controller;

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
import ploting_server.ploting.comment.dto.request.CommentCreateRequest;
import ploting_server.ploting.comment.dto.request.CommentUpdateRequest;
import ploting_server.ploting.comment.service.CommentService;
import ploting_server.ploting.core.code.success.GlobalSuccessCode;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;

@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
@Tag(name = "댓글", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 생성",
            description = "댓글을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> createComment(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long postId,
            @RequestBody CommentCreateRequest commentCreateRequest) {
        commentService.createComment(Long.parseLong(principalDetails.getUsername()), postId, commentCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "댓글 수정",
            description = "댓글을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updatePost(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest commentUpdateRequest) {
        commentService.updateComment(Long.parseLong(principalDetails.getUsername()), commentId, commentUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
