package ploting_server.ploting.filter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ploting_server.ploting.core.response.BfResponse;
import ploting_server.ploting.core.security.principal.PrincipalDetails;
import ploting_server.ploting.filter.dto.response.FilteredResponse;
import ploting_server.ploting.filter.service.FilterService;

import java.util.List;

@RestController
@RequestMapping("filters")
@RequiredArgsConstructor
@Tag(name = "필터", description = "필터 관련 API")
public class FilterController {

    private final FilterService filterService;

    @Operation(
            summary = "모든 게시글과 모임 조회",
            description = "모든 게시글과 모임을 생성일자 정렬로 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모든 게시글과 모임 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FilteredResponse.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"code\": 200,\n" +
                                    "  \"message\": \"정상 처리되었습니다.\",\n" +
                                    "  \"data\": [\n" +
                                    "    {\n" +
                                    "      \"type\": \"POST\",\n" +
                                    "      \"body\": {\n" +
                                    "        \"id\": 1,\n" +
                                    "        \"title\": \"귤껍질 어디다 버리는지 아시나요\",\n" +
                                    "        \"content\": \"귤껍질 어디에 버리는지 모르겠어요\",\n" +
                                    "        \"authorNickName\": \"twocowsilver\",\n" +
                                    "        \"likeCount\": 0,\n" +
                                    "        \"commentCount\": 0,\n" +
                                    "        \"createdAt\": \"2024-11-23T20:30:37.954789\"\n" +
                                    "      }\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"type\": \"MEETING\",\n" +
                                    "      \"body\": {\n" +
                                    "        \"id\": 1,\n" +
                                    "        \"name\": \"환경지킴이 모임\",\n" +
                                    "        \"meetDate\": \"2024-08-17\",\n" +
                                    "        \"meetHour\": 11,\n" +
                                    "        \"location\": \"강서구\",\n" +
                                    "        \"minLevel\": 5,\n" +
                                    "        \"memberCount\": 1,\n" +
                                    "        \"maxMember\": 50,\n" +
                                    "        \"minAge\": 20,\n" +
                                    "        \"maxAge\": 27,\n" +
                                    "        \"maleCount\": 0,\n" +
                                    "        \"femaleCount\": 1,\n" +
                                    "        \"createdAt\": \"2024-11-22T01:00:06.023583\"\n" +
                                    "      }\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}")
                    )
            )
    })
    @GetMapping("/main")
    public ResponseEntity<BfResponse<List<FilteredResponse>>> getMainItems(
            @RequestParam(required = false) String search
    ) {
        List<FilteredResponse> mainItems = filterService.getMainItems(search);
        return ResponseEntity.ok(new BfResponse<>(mainItems));
    }

    @Operation(
            summary = "좋아요한 단체, 모임, 게시글 조회",
            description = "좋아요한 단체, 모임, 게시글을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요한 단체, 모임, 게시글 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FilteredResponse.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"code\": 200,\n" +
                                    "  \"message\": \"정상 처리되었습니다.\",\n" +
                                    "  \"data\": [\n" +
                                    "    {\n" +
                                    "      \"type\": \"POST\",\n" +
                                    "      \"body\": {\n" +
                                    "        \"id\": 1,\n" +
                                    "        \"title\": \"귤껍질 어디다 버리는지 아시나요\",\n" +
                                    "        \"content\": \"귤껍질 어디에 버리는지 모르겠어요\",\n" +
                                    "        \"authorNickName\": \"twocowsilver\",\n" +
                                    "        \"likeCount\": 1,\n" +
                                    "        \"commentCount\": 0,\n" +
                                    "        \"createdAt\": \"2024-11-23T20:30:37.954789\"\n" +
                                    "      }\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"type\": \"MEETING\",\n" +
                                    "      \"body\": {\n" +
                                    "        \"id\": 2,\n" +
                                    "        \"name\": \"환경지킴이 모임\",\n" +
                                    "        \"meetDate\": \"2024-08-17\",\n" +
                                    "        \"meetHour\": 11,\n" +
                                    "        \"location\": \"강서구\",\n" +
                                    "        \"minLevel\": 5,\n" +
                                    "        \"memberCount\": 1,\n" +
                                    "        \"maxMember\": 50,\n" +
                                    "        \"minAge\": 20,\n" +
                                    "        \"maxAge\": 27,\n" +
                                    "        \"maleCount\": 0,\n" +
                                    "        \"femaleCount\": 1,\n" +
                                    "        \"createdAt\": \"2024-11-22T01:00:09.834355\"\n" +
                                    "      }\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"type\": \"ORGANIZATION\",\n" +
                                    "      \"body\": {\n" +
                                    "        \"id\": 2,\n" +
                                    "        \"location\": \"강서구\",\n" +
                                    "        \"name\": \"환경지킴이 단체\",\n" +
                                    "        \"description\": \"모임에 대한 설명\",\n" +
                                    "        \"minLevel\": 5,\n" +
                                    "        \"memberCount\": 1,\n" +
                                    "        \"maxMember\": 50,\n" +
                                    "        \"minAge\": 20,\n" +
                                    "        \"maxAge\": 27,\n" +
                                    "        \"maleCount\": 0,\n" +
                                    "        \"femaleCount\": 1,\n" +
                                    "        \"createdAt\": \"2024-11-22T00:59:38.696901\"\n" +
                                    "      }\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}")
                    )
            )
    })
    @GetMapping("/like")
    public ResponseEntity<BfResponse<List<FilteredResponse>>> getLikedItems(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<FilteredResponse> likedItems = filterService.getLikedItems(Long.parseLong(principalDetails.getUsername()));
        return ResponseEntity.ok(new BfResponse<>(likedItems));
    }
}
