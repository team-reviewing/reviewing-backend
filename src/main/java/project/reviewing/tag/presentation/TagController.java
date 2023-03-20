package project.reviewing.tag.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.tag.query.application.TagQueryService;
import project.reviewing.tag.query.application.response.CategoriesResponse;

@RequiredArgsConstructor
@RequestMapping("/tags")
@RestController
public class TagController {

    private final TagQueryService tagQueryService;

    @GetMapping
    public CategoriesResponse findTags() {
        return tagQueryService.findTags();
    }
}
