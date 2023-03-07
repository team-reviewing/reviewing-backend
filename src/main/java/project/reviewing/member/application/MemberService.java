package project.reviewing.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.application.response.MemberResponse;
import project.reviewing.member.domain.Member;
import project.reviewing.member.domain.MemberRepository;
import project.reviewing.member.exception.MemberException;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse findMemberProfile(final Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorType.NOT_FOUND_MEMBER));
        return new MemberResponse(member.getImageURL());
    }
}
