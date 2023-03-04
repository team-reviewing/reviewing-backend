package project.reviewing.member.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member entity);
    Optional<Member> findById(Long id);
}
