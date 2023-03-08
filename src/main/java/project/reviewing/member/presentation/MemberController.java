package project.reviewing.member.presentation;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.command.application.request.ReviewerRegistrationRequest;
import project.reviewing.member.command.application.request.UpdatingMemberRequest;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.member.query.response.MyInformationResponse;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;

    @GetMapping("/me")
    public MyInformationResponse findMyInformation(final Long memberId) {
        return memberQueryService.findMember(memberId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/me")
    public void updateMember(
            final Long memberId,
            @Valid @RequestBody final UpdatingMemberRequest updatingMemberRequest
    ) {
        memberService.update(memberId, updatingMemberRequest);
    }

    @PostMapping("/me/reviewer")
    public void registerReviewer(
            final Long memberId,
            @Valid @RequestBody final ReviewerRegistrationRequest reviewerRegistrationRequest
    ) {
        memberService.registerReviewer(memberId, reviewerRegistrationRequest);
    }
}
