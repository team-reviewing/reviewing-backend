package project.reviewing.review.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.review.command.application.ReviewService;
import project.reviewing.review.command.application.response.SingleReviewReadResponse;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.presentation.data.RoleInReview;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;
import project.reviewing.review.query.application.ReviewQueryService;
import project.reviewing.review.query.application.response.ReviewsResponse;

import javax.validation.Valid;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewQueryService reviewQueryService;

    @PostMapping("/reviewers/{reviewer-id}/reviews")
    public void createReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("reviewer-id") final Long reviewerId,
            @Valid @RequestBody final ReviewCreateRequest request
    ) {
        reviewService.createReview(memberId, reviewerId, request);
    }

    @GetMapping("/reviewers/{reviewer-id}/reviews/{review-id}")
    public SingleReviewReadResponse readSingleReview(@PathVariable("review-id") final Long reviewId) {
        return reviewService.readSingleReview(reviewId);
    }

    @GetMapping("/reviews")
    public ReviewsResponse readReviewsByRole(
            @AuthenticatedMember final Long memberId,
            @RequestParam final String role,
            @RequestParam(required = false) final String status
    ) {
        return reviewQueryService.findReviewsByRole(memberId, RoleInReview.findValue(role), ReviewStatus.of(status));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/reviewers/{reviewer-id}/reviews/{review-id}")
    public void updateReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId,
            @Valid @RequestBody final ReviewUpdateRequest request
    ) {
        reviewService.updateReview(memberId, reviewId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/reviewers/{reviewer-id}/reviews/{review-id}/status-accepted")
    public void acceptReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
        reviewService.acceptReview(memberId, reviewId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/reviewers/{reviewer-id}/reviews/{review-id}/status-refused")
    public void refuseReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
        reviewService.refuseReview(memberId, reviewId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/reviewers/{reviewer-id}/reviews/{review-id}/status-approved")
    public void approveReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
        reviewService.approveReview(memberId, reviewId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/reviewers/{reviewer-id}/reviews/{review-id}")
    public void closeReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
        reviewService.closeReview(memberId, reviewId);
    }
}
