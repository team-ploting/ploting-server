package ploting_server.ploting.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploting_server.ploting.core.code.error.MemberErrorCode;
import ploting_server.ploting.core.exception.MemberException;
import ploting_server.ploting.member.dto.request.MemberRegisterRequest;
import ploting_server.ploting.member.dto.response.MemberInfoResponse;
import ploting_server.ploting.member.dto.response.MemberNicknameDuplicationResponse;
import ploting_server.ploting.member.entity.Member;
import ploting_server.ploting.member.repository.MemberRepository;

/**
 * 회원을 관리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입 시 회원 정보를 추가적으로 등록합니다.
     */
    @Transactional
    public void registerMember(Long memberId, MemberRegisterRequest memberRegisterRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        findMember.registerMember(memberRegisterRequest);
    }

    /**
     * 회원의 정보를 반환합니다.
     */
    public MemberInfoResponse getMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        return MemberInfoResponse.builder()
                .profileImageUrl(findMember.getProfileImageUrl())
                .name(findMember.getName())
                .nickname(findMember.getNickname())
                .location(findMember.getLocation())
                .gender(findMember.getGender())
                .birth(findMember.getBirth())
                .createdAt(findMember.getCreatedAt())
                .activeStatus(findMember.isActiveStatus())
                .build();
    }
}
