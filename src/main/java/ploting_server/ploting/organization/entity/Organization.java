package ploting_server.ploting.organization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.organization.dto.request.OrganizationUpdateRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "member_count")
    private int memberCount;

    @Column(name = "male_count")
    private int maleCount;

    @Column(name = "female_count")
    private int femaleCount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 양방향 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrganizationMember> organizationMembers = new ArrayList<>();

    // 연관관계 편의 메서드 - 추가
    public void addOrganizationMember(OrganizationMember organizationMember) {
        this.organizationMembers.add(organizationMember);
    }

    // 연관관계 편의 메서드 - 삭제
    public void removeOrganizationMember(OrganizationMember organizationMember) {
        this.organizationMembers.remove(organizationMember);
    }

    /**
     * 단체 수정
     */
    public void updateOrganization(OrganizationUpdateRequest organizationUpdateRequest) {
        this.name = organizationUpdateRequest.getName();
        this.description = organizationUpdateRequest.getDescription();
        this.location = organizationUpdateRequest.getLocation();
        this.maxMember = organizationUpdateRequest.getMaxMember();
        this.minAge = organizationUpdateRequest.getMinAge();
        this.maxAge = organizationUpdateRequest.getMaxAge();
        this.minLevel = organizationUpdateRequest.getMinLevel();
        this.organizationImageUrl = organizationUpdateRequest.getOrganizationImageUrl();
    }

    /**
     * 멤버 수 증가
     */
    public void incrementMemberCount() {
        this.memberCount++;
    }

    /**
     * 멤버 수 감소
     */
    public void decrementMemberCount() {
        this.memberCount--;
    }

    /**
     * 여자 수 증가
     */
    public void incrementFemaleCount() {
        this.femaleCount++;
    }

    /**
     * 여자 수 감소
     */
    public void decrementFemaleCount() {
        this.femaleCount--;
    }

    /**
     * 남자 수 증가
     */
    public void incrementMaleCount() {
        this.maleCount++;
    }

    /**
     * 남자 수 감소
     */
    public void decrementMaleCount() {
        this.maleCount--;
    }
}
