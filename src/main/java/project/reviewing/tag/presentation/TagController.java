package project.reviewing.tag.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tags")
@RestController
public class TagController {

    @GetMapping
    public void findTags() {

    }
}
