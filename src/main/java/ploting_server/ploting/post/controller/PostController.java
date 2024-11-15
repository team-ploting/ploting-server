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
import ploting_server.ploting.post.dto.request.PostCreateRequest;
import ploting_server.ploting.post.dto.request.PostUpdateRequest;
import ploting_server.ploting.post.service.PostService;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
@Tag(name = "게시글", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "게시글 생성",
            description = "게시글을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 생성 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PostMapping("")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> createPost(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody PostCreateRequest postCreateRequest) {
        postService.createPost(Long.parseLong(principalDetails.getUsername()), postCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "게시글 수정",
            description = "게시글을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("/{postId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updatePost(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.updatePost(Long.parseLong(principalDetails.getUsername()), postId, postUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "게시글 삭제",
            description = "게시글을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deletePost(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long postId) {
        postService.deletePost(Long.parseLong(principalDetails.getUsername()), postId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
