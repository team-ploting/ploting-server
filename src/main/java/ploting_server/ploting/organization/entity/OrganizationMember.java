package ploting_server.ploting.organization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.member.entity.Member;

import java.time.LocalDateTime;

/**
 * 단체와 회원의 중간 테이블을 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrganizationMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JoinColumn(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "leader_status")
    private boolean leaderStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 새로운 단체장에게 권한을 부여
    public void assignLeader() {
        this.leaderStatus = true;
    }

    // 기존 단체장의 권한을 해제
    public void revokeLeader() {
        this.leaderStatus = false;
    }
}
