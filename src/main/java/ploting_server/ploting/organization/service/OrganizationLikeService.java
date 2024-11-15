package ploting_server.ploting.organization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.code.error.OrganizationErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.core.exception.OrganizationException;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;
import ploting_server.ploting.organization.entity.Organization;
import ploting_server.ploting.organization.entity.OrganizationLike;
import ploting_server.ploting.organization.repository.OrganizationLikeRepository;
import ploting_server.ploting.organization.repository.OrganizationRepository;

import java.util.Optional;

/**
 * 단체 좋아요를 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class OrganizationLikeService {

    private final OrganizationLikeRepository organizationLikeRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    /**
     * 단체의 좋아요를 누릅니다.
     */
    @Transactional
    public void pressOrganizationLike(Long memberId, Long organizationId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        Optional<OrganizationLike> checkOrganizationLike = organizationLikeRepository.findByMemberIdAndOrganizationId(memberId, organizationId);

        if (checkOrganizationLike.isPresent()) {
            throw new OrganizationException(OrganizationErrorCode.ALREADY_EXIST_ORGANIZATION_LIKE);
        }

        OrganizationLike organizationLike = OrganizationLike.builder()
                .organization(organization)
                .member(member)
                .build();

        // 좋아요 수 증가
        organization.incrementLikeCount();

        organizationLikeRepository.save(organizationLike);
    }

    /**
     * 단체의 좋아요를 취소합니다.
     */
    @Transactional
    public void cancelOrganizationLike(Long memberId, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_ID));

        OrganizationLike organizationLike = organizationLikeRepository.findByMemberIdAndOrganizationId(memberId, organizationId)
                .orElseThrow(() -> new OrganizationException(OrganizationErrorCode.NOT_FOUND_ORGANIZATION_LIKE));

        // 좋아요 수 감소
        organization.decrementLikeCount();

        organizationLikeRepository.delete(organizationLike);
    }
}
