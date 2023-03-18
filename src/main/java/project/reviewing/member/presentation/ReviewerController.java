package project.reviewing.member.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.member.query.application.response.ReviewersResponse;

@RequestMapping("/reviewers")
@RequiredArgsConstructor
@RestController
public class ReviewerController {

    private final MemberQueryService memberQueryService;

    @GetMapping
    public ReviewersResponse findReviewers(
            @PageableDefault(size = 9) final Pageable pageable,
            @RequestParam(name = "category", required = false) final Long categoryId,
            @RequestParam(name = "tag", required = false) final List<Long> tagIds
    ) {
        return memberQueryService.findReviewers(pageable, categoryId, tagIds);
    }
}
