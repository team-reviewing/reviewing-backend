package project.reviewing.member.command.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member entity);
    Optional<Member> findById(Long id);
    Optional<Member> findByGithubId(Long githubId);
}
