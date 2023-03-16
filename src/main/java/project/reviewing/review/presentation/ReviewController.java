package project.reviewing.review.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.review.application.ReviewService;
import project.reviewing.review.presentation.request.ReviewCreateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@RequestMapping("/reviewers")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{id}/reviews")
    public void createReview(
            @AuthenticatedMember final Long memberId,
            @PathVariable("id") @Min(1) final Long reviewerId,
            @Valid @RequestBody final ReviewCreateRequest reviewCreateRequest
    ) {
        reviewService.createReview(memberId, reviewerId, reviewCreateRequest);
    }
}
