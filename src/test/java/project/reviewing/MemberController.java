package project.reviewing;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberController {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/me")
    public void updateMember(@Valid @RequestBody final UpdatingMemberRequest updatingMemberRequest) {

    }
}
