package ploting_server.ploting.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ploting_server.ploting.comment.entity.Comment;
import ploting_server.ploting.member.dto.request.MemberRegisterRequest;
import ploting_server.ploting.member.dto.request.MemberUpdateRequest;
import ploting_server.ploting.post.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Post 양방향 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // Comment 양방향 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Post 연관관계 편의 메서드 - 추가
    public void addPost(Post post) {
        this.posts.add(post);
    }

    // Post 연관관계 편의 메서드 - 삭제
    public void removePost(Post post) {
        this.posts.remove(post);
    }

    // Comment 연관관계 편의 메서드 - 추가
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    // Comment 연관관계 편의 메서드 - 삭제
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    // 회원 가입 시 정보 업데이트
    public void registerMember(MemberRegisterRequest memberRegisterRequest) {
        this.name = memberRegisterRequest.getName();
        this.nickname = memberRegisterRequest.getNickname();
        this.location = memberRegisterRequest.getLocation();
        this.gender = memberRegisterRequest.getGender();
        this.birth = memberRegisterRequest.getBirth();
        this.createdAt = LocalDateTime.now();
    }

    // 회원 정보 수정
    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        this.name = memberUpdateRequest.getName();
        this.nickname = memberUpdateRequest.getNickname();
        this.location = memberUpdateRequest.getLocation();
        this.birth = memberUpdateRequest.getBirth();
    }
}
