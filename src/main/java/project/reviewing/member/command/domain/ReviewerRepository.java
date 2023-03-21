package project.reviewing.member.command.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ReviewerRepository extends Repository<Reviewer, Long> {

    Optional<Reviewer> findById(Long id);
}
