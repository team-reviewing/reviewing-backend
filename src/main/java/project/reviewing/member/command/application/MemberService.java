package project.reviewing.member.command.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.member.command.application.request.UpdatingMemberRequest;
import project.reviewing.member.query.response.MyInformationResponse;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void update(final Long memberId, final UpdatingMemberRequest updatingMemberRequest) {
        final Member member = getMember(memberId);
        final Member updatedMember = updatingMemberRequest.toEntity();

        member.update(updatedMember);
    }

    public MyInformationResponse findMember(final Long memberId) {
        return MyInformationResponse.of(getMember(memberId));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
