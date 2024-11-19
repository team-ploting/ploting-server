package ploting_server.ploting.point.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.mission.entity.Mission;

import java.time.LocalDateTime;

/**
 * 포인트를 관리하는 미션과 회원의 중간 테이블 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
