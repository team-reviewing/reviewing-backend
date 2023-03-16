package project.reviewing.review.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.review.application.ReviewService;
import project.reviewing.review.presentation.request.ReviewCreateRequest;

import javax.validation.Valid;

@RequestMapping(value = "/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public void createReview(
            @AuthenticatedMember final Long memberId,
            @Valid @RequestBody final ReviewCreateRequest reviewCreateRequest
    ) {
        reviewService.createReview(memberId, reviewCreateRequest);
    }
}
