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
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.presentation.request.MyInformationUpdateRequest;
import project.reviewing.member.presentation.request.ReviewerRegistrationRequest;
import project.reviewing.member.presentation.request.ReviewerUpdateRequest;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.member.query.application.response.MyReviewerInformationResponse;
import project.reviewing.member.query.application.response.MyInformationResponse;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;

    @GetMapping("/me")
    public MyInformationResponse findMyInformation(@AuthenticatedMember final Long memberId) {
        return memberQueryService.findMember(memberId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/me")
    public void updateMember(
            @AuthenticatedMember final Long memberId,
            @Valid @RequestBody final MyInformationUpdateRequest myInformationUpdateRequest
    ) {
        memberService.update(memberId, myInformationUpdateRequest);
    }

    @PostMapping("/me/reviewer")
    public void registerReviewer(
            @AuthenticatedMember final Long memberId,
            @Valid @RequestBody final ReviewerRegistrationRequest reviewerRegistrationRequest
    ) {
        memberService.registerReviewer(memberId, reviewerRegistrationRequest);
    }

    @GetMapping("/me/reviewer")
    public MyReviewerInformationResponse findReviewerInformation(@AuthenticatedMember final Long memberId) {
        return memberQueryService.findReviewerWithChoiceList(memberId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/me/reviewer")
    public void updateReviewer(
            @AuthenticatedMember final Long memberId,
            @Valid @RequestBody final ReviewerUpdateRequest reviewerUpdateRequest
    ) {
        memberService.updateReviewer(memberId, reviewerUpdateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/me/reviewer-status")
    public void changeReviewerStatus(@AuthenticatedMember final Long memberId) {
        memberService.changeReviewerStatus(memberId);
    }
}
