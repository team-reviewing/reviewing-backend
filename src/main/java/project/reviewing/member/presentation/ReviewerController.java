package project.reviewing.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reviewers")
@RequiredArgsConstructor
@RestController
public class ReviewerController {

    @GetMapping
    public void findReviewers() {

    }
}
