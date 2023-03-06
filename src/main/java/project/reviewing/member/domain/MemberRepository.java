package project.reviewing.member.domain;

import java.util.Optional;

public interface MemberRepository {

    Member save(final Member member);
    Optional<Member> findById(final Long id);
    Member findByGithubId(final Long githubId);
}
