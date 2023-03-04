package project.reviewing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberController {

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping("/me")
    public void updateMember(@RequestBody final UpdatingMemberRequest updatingMemberRequest) {

    }
}
