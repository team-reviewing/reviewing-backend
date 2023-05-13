package project.reviewing.evaluation.command.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface EvaluationRepository extends Repository<Evaluation, Long> {

    boolean existsByReviewId(Long reviewId);
    Evaluation save(Evaluation entity);
    Optional<Evaluation> findById(Long id);
    Optional<Evaluation> findByReviewId(Long reviewId);
}
