package project.reviewing.evaluation.domain;

import org.springframework.data.repository.Repository;

public interface EvaluationRepository extends Repository<Evaluation, Long> {

    void save(Evaluation entity);
}
