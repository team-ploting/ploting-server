package ploting_server.ploting.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ploting_server.ploting.core.code.error.PostErrorCode;
import ploting_server.ploting.core.exception.PostException;
import ploting_server.ploting.main.dto.response.FilteredResponse;
import ploting_server.ploting.main.entity.FilterType;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.post.dto.response.PostListResponse;
import ploting_server.ploting.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 메인을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MainService {

    private final PostRepository postRepository;
    private final MeetingRepository meetingRepository;

    /**
     * 게시글과 모임을 조회합니다.
     */
    public List<FilteredResponse> getFilteredPostAndMeeting(String search) {
        List<FilteredResponse> posts = postRepository.searchPosts(search).stream()
                .map(post -> FilteredResponse.builder()
                        .type(FilterType.POST)
                        .body(PostListResponse.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .authorNickName(post.getMember().getNickname())
                                .likeCount(post.getLikeCount())
                                .commentCount(post.getCommentCount())
                                .createdAt(post.getCreatedAt())
                                .build())
                        .build())
                .toList();

        List<FilteredResponse> meetings = meetingRepository.searchMeetings(search).stream()
                .map(meeting -> FilteredResponse.builder()
                        .type(FilterType.MEETING)
                        .body(MeetingListResponse.builder()
                                .id(meeting.getId())
                                .name(meeting.getName())
                                .meetDate(meeting.getMeetDate().toLocalDate())
                                .meetHour(meeting.getMeetDate().getHour())
                                .location(meeting.getLocation())
                                .minLevel(meeting.getMinLevel())
                                .memberCount(meeting.getMemberCount())
                                .maxMember(meeting.getMaxMember())
                                .minAge(meeting.getMinAge())
                                .maxAge(meeting.getMaxAge())
                                .maleCount(meeting.getMaleCount())
                                .femaleCount(meeting.getFemaleCount())
                                .createdAt(meeting.getCreatedAt())
                                .build())
                        .build())
                .toList();

        List<FilteredResponse> combinedList = new ArrayList<>();
        combinedList.addAll(posts);
        combinedList.addAll(meetings);

        combinedList.sort(Comparator.comparing((FilteredResponse item) -> {
            if (item.getBody() instanceof MeetingListResponse) {
                return ((MeetingListResponse) item.getBody()).getCreatedAt();
            }
            if (item.getBody() instanceof PostListResponse) {
                return ((PostListResponse) item.getBody()).getCreatedAt();
            }
            throw new PostException(PostErrorCode.INVALID_FILTER_DATA_TYPE);
        }).reversed());

        return combinedList;
    }
}
