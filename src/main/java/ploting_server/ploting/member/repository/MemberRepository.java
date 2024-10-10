package ploting_server.ploting.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.entity.RolesType;

import java.util.Optional;

/**
 * 회원 도메인의 Repository 입니다.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthIdAndRoles(String oauthId, RolesType rolesType);
}