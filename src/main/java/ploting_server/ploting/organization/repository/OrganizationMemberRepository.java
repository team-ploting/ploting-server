package ploting_server.ploting.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.organization.entity.OrganizationMember;

import java.util.List;
import java.util.Optional;

/**
 * OrganizationMember의 Repository 입니다.
 */
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {
    List<OrganizationMember> findAllByOrganizationId(Long organizationId);

    List<OrganizationMember> findAllByMemberId(Long memberId);

    Optional<OrganizationMember> findByOrganizationIdAndMemberId(Long organizationId, Long memberId);

    Optional<OrganizationMember> findByOrganizationIdAndLeaderStatusTrue(Long organizationId);

    boolean existsByOrganizationIdAndMemberId(Long organizationId, Long memberId);
}
