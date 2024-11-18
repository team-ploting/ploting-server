package ploting_server.ploting.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ploting_server.ploting.organization.entity.Organization;

import java.util.List;

/**
 * 단체 도메인의 Repository 입니다.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query("SELECT o FROM Organization o ORDER BY o.createdAt DESC")
    List<Organization> findAllOrderByCreatedAtDesc();

    @Query("select o from Organization o join o.organizationMembers om where om.member.id = :memberId ORDER BY o.createdAt DESC")
    List<Organization> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}
