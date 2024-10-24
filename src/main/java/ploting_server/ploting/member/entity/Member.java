package ploting_server.ploting.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.member.dto.request.MemberRegisterRequest;
import ploting_server.ploting.member.dto.request.MemberUpdateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원을 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "location")
    private String location;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "level")
    private int level;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "active_status")
    private boolean activeStatus; // soft delete 용도

    // 회원 가입 시 정보 업데이트
    public void registerMember(MemberRegisterRequest memberRegisterRequest) {
        this.name = memberRegisterRequest.getName();
        this.nickname = memberRegisterRequest.getNickname();
        this.location = memberRegisterRequest.getLocation();
        this.gender = memberRegisterRequest.getGender();
        this.birth = memberRegisterRequest.getBirth();
        this.createdAt = LocalDateTime.now();
        this.activeStatus = true;
    }

    // 회원 탈퇴 (soft delete)
    public void softDeleteMember() {
        this.activeStatus = false;
    }

    // 회원 정보 수정
    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        this.name = memberUpdateRequest.getName();
        this.nickname = memberUpdateRequest.getNickname();
        this.location = memberUpdateRequest.getLocation();
        this.birth = memberUpdateRequest.getBirth();
    }
}
