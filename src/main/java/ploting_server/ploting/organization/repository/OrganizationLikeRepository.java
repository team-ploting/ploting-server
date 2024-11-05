package ploting_server.ploting.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.organization.entity.OrganizationLike;

import java.util.List;
import java.util.Optional;

/**
 * OrganizationLike의 Repository 입니다.
 */
public interface OrganizationLikeRepository extends JpaRepository<OrganizationLike, Long> {
    List<OrganizationLike> findAllByMemberId(Long memberId);

    List<OrganizationLike> findAllByOrganizationId(Long organizationId);

    boolean existsByMemberIdAndOrganizationId(Long memberId, Long organizationId);

    Optional<OrganizationLike> findByMemberIdAndOrganizationId(Long memberId, Long organizationId);
}
