package project.reviewing.member.command.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member entity);
    Optional<Member> findById(Long id);
    Optional<Member> findByGithubId(Long githubId);

    @Query("SELECT m FROM Member m JOIN FETCH m.reviewer r WHERE r.id = :reviewerId")
    Optional<Member> findByReviewerId(@Param("reviewerId") Long reviewerId);
}
