package ploting_server.ploting.filter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.FilterErrorCode;
import ploting_server.ploting.core.exception.FilterException;
import ploting_server.ploting.filter.dto.response.FilteredResponse;
import ploting_server.ploting.filter.entity.FilterType;
import ploting_server.ploting.meeting.dto.response.MeetingListResponse;
import ploting_server.ploting.meeting.repository.MeetingLikeRepository;
import ploting_server.ploting.meeting.repository.MeetingRepository;
import ploting_server.ploting.organization.dto.response.OrganizationListResponse;
import ploting_server.ploting.organization.repository.OrganizationLikeRepository;
import ploting_server.ploting.post.dto.response.PostListResponse;
import ploting_server.ploting.post.entity.Post;
import ploting_server.ploting.post.repository.PostLikeRepository;
import ploting_server.ploting.post.repository.PostRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 메인을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class FilterService {

    private final PostRepository postRepository;
    private final MeetingRepository meetingRepository;
    private final OrganizationLikeRepository organizationLikeRepository;
    private final MeetingLikeRepository meetingLikeRepository;
    private final PostLikeRepository postLikeRepository;

    /**
     * 게시글과 모임을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<FilteredResponse> getMainItems(String search) {
        List<Post> searchPosts = postRepository.searchPosts(search);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter meetFormatter = DateTimeFormatter.ofPattern("M월 d일 a h시", Locale.KOREAN);

        List<FilteredResponse> posts = searchPosts.stream()
                .map(post -> FilteredResponse.builder()
                        .type(FilterType.POST)
                        .body(PostListResponse.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .authorNickName(post.getMember().getNickname())
                                .likeCount(post.getLikeCount())
                                .commentCount(post.getCommentCount())
                                .createdDate(post.getCreatedAt().format(dateFormatter))
                                .createdTime(post.getCreatedAt().format(timeFormatter))
                                .build())
                        .build())
                .toList();

        List<FilteredResponse> meetings = meetingRepository.searchMeetings(search).stream()
                .map(meeting -> FilteredResponse.builder()
                        .type(FilterType.MEETING)
                        .body(MeetingListResponse.builder()
                                .id(meeting.getId())
                                .activeStatus(meeting.isActiveStatus())
                                .name(meeting.getName())
                                .meetDate(meeting.getMeetDate().format(meetFormatter))
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
                PostListResponse body = (PostListResponse) item.getBody();
                return LocalDateTime.parse(
                        body.getCreatedDate() + " " + body.getCreatedTime(),
                        DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")
                );
            }
            throw new FilterException(FilterErrorCode.INVALID_FILTER_DATA_TYPE);
        }).reversed());

        return combinedList;
    }

    /**
     * 좋아요를 누른 단체, 모임, 게시글을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<FilteredResponse> getLikedItems(Long memberId) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter meetFormatter = DateTimeFormatter.ofPattern("M월 d일 a h시", Locale.KOREAN);

        List<FilteredResponse> organizations = organizationLikeRepository.findAllByMemberId(memberId).stream()
                .map(organizationLike -> FilteredResponse.builder()
                        .type(FilterType.ORGANIZATION)
                        .body(OrganizationListResponse.builder()
                                .id(organizationLike.getOrganization().getId())
                                .location(organizationLike.getOrganization().getLocation())
                                .name(organizationLike.getOrganization().getName())
                                .description(organizationLike.getOrganization().getDescription())
                                .minLevel(organizationLike.getOrganization().getMinLevel())
                                .memberCount(organizationLike.getOrganization().getMemberCount())
                                .maxMember(organizationLike.getOrganization().getMaxMember())
                                .minAge(organizationLike.getOrganization().getMinAge())
                                .maxAge(organizationLike.getOrganization().getMaxAge())
                                .maleCount(organizationLike.getOrganization().getMaleCount())
                                .femaleCount(organizationLike.getOrganization().getFemaleCount())
                                .createdAt(organizationLike.getOrganization().getCreatedAt())
                                .build())
                        .build())
                .toList();

        List<FilteredResponse> meetings = meetingLikeRepository.findAllByMemberId(memberId).stream()
                .map(meetingLike -> FilteredResponse.builder()
                        .type(FilterType.MEETING)
                        .body(MeetingListResponse.builder()
                                .id(meetingLike.getMeeting().getId())
                                .name(meetingLike.getMeeting().getName())
                                .meetDate(meetingLike.getMeeting().getMeetDate().format(meetFormatter))
                                .location(meetingLike.getMeeting().getLocation())
                                .minLevel(meetingLike.getMeeting().getMinLevel())
                                .memberCount(meetingLike.getMeeting().getMemberCount())
                                .maxMember(meetingLike.getMeeting().getMaxMember())
                                .minAge(meetingLike.getMeeting().getMinAge())
                                .maxAge(meetingLike.getMeeting().getMaxAge())
                                .maleCount(meetingLike.getMeeting().getMaleCount())
                                .femaleCount(meetingLike.getMeeting().getFemaleCount())
                                .createdAt(meetingLike.getMeeting().getCreatedAt())
                                .build())
                        .build())
                .toList();

        List<FilteredResponse> posts = postLikeRepository.findAllByMemberId(memberId).stream()
                .map(postLike -> FilteredResponse.builder()
                        .type(FilterType.POST)
                        .body(PostListResponse.builder()
                                .id(postLike.getPost().getId())
                                .title(postLike.getPost().getTitle())
                                .content(postLike.getPost().getContent())
                                .authorNickName(postLike.getPost().getMember().getNickname())
                                .likeCount(postLike.getPost().getLikeCount())
                                .commentCount(postLike.getPost().getCommentCount())
                                .createdDate(postLike.getPost().getCreatedAt().format(dateFormatter))
                                .createdTime(postLike.getPost().getCreatedAt().format(timeFormatter)).build())
                        .build())
                .toList();

        List<FilteredResponse> combinedList = new ArrayList<>();
        combinedList.addAll(organizations);
        combinedList.addAll(meetings);
        combinedList.addAll(posts);

        combinedList.sort(Comparator.comparing((FilteredResponse item) -> {
            if (item.getBody() instanceof OrganizationListResponse) {
                return ((OrganizationListResponse) item.getBody()).getCreatedAt();
            }
            if (item.getBody() instanceof MeetingListResponse) {
                return ((MeetingListResponse) item.getBody()).getCreatedAt();
            }
            if (item.getBody() instanceof PostListResponse) {
                PostListResponse body = (PostListResponse) item.getBody();
                return LocalDateTime.parse(
                        body.getCreatedDate() + " " + body.getCreatedTime(),
                        DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")
                );
            }
            throw new FilterException(FilterErrorCode.INVALID_FILTER_DATA_TYPE);
        }).reversed());

        return combinedList;
    }
}
