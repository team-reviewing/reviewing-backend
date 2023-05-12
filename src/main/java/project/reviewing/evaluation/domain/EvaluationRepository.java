package project.reviewing.evaluation.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface EvaluationRepository extends Repository<Evaluation, Long> {

    boolean existsByReviewId(Long reviewId);
    Evaluation save(Evaluation entity);
    Optional<Evaluation> findById(Long id);
}
