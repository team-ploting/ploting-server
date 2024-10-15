package ploting_server.ploting.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ploting_server.ploting.organization.entity.Organization;

/**
 * 단체 도메인의 Repository 입니다.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
