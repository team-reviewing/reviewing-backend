package project.reviewing.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.reviewing.member.domain.Member;
import project.reviewing.member.domain.MemberRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepository {
}
