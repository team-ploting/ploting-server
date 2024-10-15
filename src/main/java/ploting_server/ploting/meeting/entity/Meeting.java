package ploting_server.ploting.meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.organization.entity.Organization;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

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

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "active_status")
    private boolean activeStatus;
}
