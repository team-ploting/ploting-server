package ploting_server.ploting.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.organization.entity.OrganizationMember;

import java.util.Optional;

/**
 * OrganizationMember의 Repository 입니다.
 */
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {
    Optional<OrganizationMember> findByOrganizationIdAndLeaderStatusTrue(Long organizationId);
}
