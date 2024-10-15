package ploting_server.ploting.organization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.dto.request.OrganizationCreateRequest;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.organization.entity.OrganizationMember;
import ploting_server.ploting.organization.repository.OrganizationMemberRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;

import java.time.LocalDateTime;

/**
 * 단체를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 단체를 생성합니다.
     */
    @Transactional
    public void createOrganization(Long memberId, OrganizationCreateRequest organizationCreateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 단체 생성
        Organization organization = Organization.builder()
                .name(organizationCreateRequest.getName())
                .description(organizationCreateRequest.getDescription())
                .location(organizationCreateRequest.getLocation())
                .maxMember(organizationCreateRequest.getMaxMember())
                .minAge(organizationCreateRequest.getMinAge())
                .maxAge(organizationCreateRequest.getMaxAge())
                .minLevel(organizationCreateRequest.getMinLevel())
                .organizationImageUrl(organizationCreateRequest.getOrganizationImageUrl())
                .likeCount(0)
                .femaleCount(0)
                .maleCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        organizationRepository.save(organization);

        // 리더로서 OrganizationMember 생성
        OrganizationMember organizationMember = OrganizationMember.builder()
                .organization(organization)
                .member(member)
                .introduction(organizationCreateRequest.getIntroduction())
                .isLeader(true)
                .createdAt(LocalDateTime.now())
                .build();

        organizationMemberRepository.save(organizationMember);
    }
}
