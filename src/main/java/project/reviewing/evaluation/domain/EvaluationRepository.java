package project.reviewing.evaluation.domain;

import org.springframework.data.repository.Repository;

public interface EvaluationRepository extends Repository<Evaluation, Long> {

    boolean existsByReviewId(Long reviewId);
    void save(Evaluation entity);
}
