package project.reviewing.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reviewers")
@RequiredArgsConstructor
@RestController
public class ReviewerController {

    @GetMapping
    public void findReviewers(
            @PageableDefault(size = 9) final Pageable pageable
    ) {

    }
}
