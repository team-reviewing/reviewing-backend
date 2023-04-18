package project.reviewing.review.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.review.command.application.ReviewService;
import project.reviewing.review.command.application.response.SingleReviewReadResponse;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;
import project.reviewing.review.query.response.ReviewsResponse;

import javax.validation.Valid;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

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
            @RequestParam(value = "role") final String role
    ) {
        return new ReviewsResponse();
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
    @DeleteMapping("/reviewers/{reviewer-id}/reviews/{review-id}")
    public void refuseReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
        reviewService.refuseReview(memberId, reviewId);
    }
}
