package ploting_server.ploting.organization.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ploting_server.ploting.organization.entity.Organization;

/**
 * 단체 도메인의 Repository 입니다.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // TODO: N+1 문제 처리
    @Query("select o from Organization o join o.organizationMembers om where om.member.id = :memberId")
    Page<Organization> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
