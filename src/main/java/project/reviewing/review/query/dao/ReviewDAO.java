package project.reviewing.review.query.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.reviewing.review.command.domain.ReviewStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@RequiredArgsConstructor
@Repository
public class ReviewDAO {

    @PersistenceContext
    private final EntityManager em;

    public Boolean existsByRevieweeIdAndReviewerIdWithNotApprovedAndEvaluated(final Long revieweeId, final Long reviewerId) {
        final String jpql = "SELECT COUNT(r) " +
                "FROM Review r " +
                "WHERE r.revieweeId = :revieweeId " +
                "AND r.reviewerId = :reviewerId " +
                "AND NOT r.status IN ('" + ReviewStatus.APPROVED.name() + "','" + ReviewStatus.EVALUATED.name() + "')";

        final Query query = em.createQuery(jpql)
                .setParameter("revieweeId", revieweeId)
                .setParameter("reviewerId", reviewerId);

        return ((Long) query.getSingleResult() > 0);
    }
}
