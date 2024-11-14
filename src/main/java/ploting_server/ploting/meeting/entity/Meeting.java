package ploting_server.ploting.meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.meeting.dto.request.MeetingUpdateRequest;
import ploting_server.ploting.member.entity.GenderType;
import ploting_server.ploting.organization.entity.Organization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 모임을 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "max_member")
    private int maxMember;

    @Column(name = "min_age")
    private int minAge;

    @Column(name = "max_age")
    private int maxAge;

    @Column(name = "min_level")
    private int minLevel;

    @Column(name = "meet_date")
    private LocalDateTime meetDate;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "member_count")
    private int memberCount;

    @Column(name = "male_count")
    private int maleCount;

    @Column(name = "female_count")
    private int femaleCount;

    @Column(name = "active_status")
    private boolean activeStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // MeetingMember 양방향 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    // MeetingMember 연관관계 편의 메서드 - 추가
    public void addMeetingMember(MeetingMember meetingMember) {
        this.meetingMembers.add(meetingMember);
    }

    // MeetingMember 연관관계 편의 메서드 - 삭제
    public void removeMeetingMember(MeetingMember meetingMember) {
        this.meetingMembers.remove(meetingMember);
    }

    /**
     * 모임 수정
     */
    public void updateMeeting(MeetingUpdateRequest meetingUpdateRequest) {
        this.name = meetingUpdateRequest.getName();
        this.location = meetingUpdateRequest.getLocation();
        this.description = meetingUpdateRequest.getDescription();
        this.maxMember = meetingUpdateRequest.getMaxMember();
        this.minAge = meetingUpdateRequest.getMinAge();
        this.maxAge = meetingUpdateRequest.getMaxAge();
        this.minLevel = meetingUpdateRequest.getMinLevel();

        LocalDate date = meetingUpdateRequest.getMeetDate();
        int hour = meetingUpdateRequest.getMeetHour();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        this.meetDate = LocalDateTime.parse(date + " " + hour, formatter);
    }

    /**
     * 멤버 수, 성별 수 증가
     */
    public void incrementMemberAndGenderCount(GenderType genderType) {
        if (genderType.equals(GenderType.MALE)) {
            this.maleCount++;
        }

        if (genderType.equals(GenderType.FEMALE)) {
            this.femaleCount++;
        }

        this.memberCount++;
    }

    /**
     * 멤버 수, 성별 수 감소
     */
    public void decrementMemberAndGenderCount(GenderType genderType) {
        if (genderType.equals(GenderType.MALE)) {
            this.maleCount--;
        }

        if (genderType.equals(GenderType.FEMALE)) {
            this.femaleCount--;
        }

        this.memberCount--;
    }

    /**
     * 좋아요 수 증가
     */
    public void incrementLikeCount() {
        this.likeCount++;
    }

    /**
     * 좋아요 수 감소
     */
    public void decrementLikeCount() {
        this.likeCount--;
    }
}
