package project.reviewing.review.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.review.application.ReviewService;
import project.reviewing.review.application.response.SingleReviewReadResponse;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;

import javax.validation.Valid;

@RequestMapping("/reviewers/{reviewer-id}/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public void createReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("reviewer-id") final Long reviewerId,
            @Valid @RequestBody final ReviewCreateRequest request
    ) {
        reviewService.createReview(memberId, reviewerId, request);
    }

    @GetMapping("/{review-id}")
    public SingleReviewReadResponse readSingleReview(@PathVariable("review-id") final Long reviewId) {
        return reviewService.readSingleReview(reviewId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{review-id}")
    public void updateReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId,
            @Valid @RequestBody final ReviewUpdateRequest request
    ) {
        reviewService.updateReview(memberId, reviewId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{review-id}/status-accepted")
    public void acceptReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
        reviewService.acceptReview(memberId, reviewId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{review-id}")
    public void refuseReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
    }
}
