package ploting_server.ploting.organization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 단체를 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "max_member")
    private int maxMember;

    @Column(name = "min_age")
    private int minAge;

    @Column(name = "max_age")
    private int maxAge;

    @Column(name = "min_level")
    private int minLevel;

    @Column(name = "organization_image_url")
    private String organizationImageUrl;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "male_count")
    private int maleCount;

    @Column(name = "female_count")
    private int femaleCount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
