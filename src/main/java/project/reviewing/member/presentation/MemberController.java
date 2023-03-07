package project.reviewing.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.member.application.MemberService;
import project.reviewing.member.application.response.MemberResponse;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping(value = "/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> findMemberProfile(final HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(memberService.findMemberProfile((long) (int) httpServletRequest.getAttribute("id")));
    }
}
