package ploting_server.ploting.meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.member.entity.Member;

import java.time.LocalDateTime;

/**
 * 모임의 좋아요를 관리하는 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MeetingLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
