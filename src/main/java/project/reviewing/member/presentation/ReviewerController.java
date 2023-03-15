package project.reviewing.member.presentation;

import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/reviewers")
@RequiredArgsConstructor
@RestController
public class ReviewerController {

    @GetMapping
    public void findReviewers(
            @PositiveOrZero @RequestParam final Long page
    ) {

    }
}
