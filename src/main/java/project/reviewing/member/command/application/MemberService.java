package project.reviewing.member.command.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.member.command.application.request.ReviewerRegistrationRequest;
import project.reviewing.member.command.application.request.ReviewerUpdateRequest;
import project.reviewing.member.command.application.request.MyInformationUpdateRequest;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void update(final Long memberId, final MyInformationUpdateRequest myInformationUpdateRequest) {
        final Member member = getMember(memberId);
        final Member updatedMember = myInformationUpdateRequest.toEntity();

        member.update(updatedMember);
    }

    public void registerReviewer(final Long memberId, final ReviewerRegistrationRequest reviewerRegistrationRequest) {
        final Member member = getMember(memberId);

        member.register(reviewerRegistrationRequest.toEntity());
    }

    public void updateReviewer(final Long memberId, final ReviewerUpdateRequest reviewerUpdateRequest) {
        final Member member = getMember(memberId);

        member.updateReviewer(reviewerUpdateRequest.toEntity());
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
