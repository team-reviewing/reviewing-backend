package project.reviewing.review.query.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.presentation.data.RoleInReview;
import project.reviewing.review.query.dao.data.ReviewByRoleData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReviewsDAO {

    @PersistenceContext
    private final EntityManager em;

    public List<ReviewByRoleData> findReviewsByRole(
            final Long memberId, final RoleInReview role, final ReviewStatus status
    ) {
        final String jpql = makeJpqlByRole(memberId, role) + makeWhereClauseForStatus(status);
        final Query query = em.createQuery(jpql)
                .setParameter("memberId", memberId);

        if (!status.isNone()) {
            query.setParameter("status", status);
        }
        return mapToReviewByRoleDataList(query.getResultList());

    }

    private String makeJpqlByRole(final Long memberId, final RoleInReview role) {
        if (role.isReviewer()) {
            return "SELECT rv.id, rv.title, rv.reviewerId, rv.status, m.id, m.username, m.imageUrl "
                    + "FROM Member m1 "
                    + "JOIN m1.reviewer rr "
                    + "JOIN Review rv ON rv.reviewerId = rr.id "
                    + "JOIN Member m ON m.id = rv.revieweeId "
                    + "WHERE m1.id = :memberId";
        } else {
            return "SELECT rv.id, rv.title, rv.reviewerId, rv.status, m.id, m.username, m.imageUrl "
                    + "FROM Member m "
                    + "JOIN m.reviewer rr "
                    + "JOIN Review rv ON rv.reviewerId = rr.id "
                    + "WHERE rv.revieweeId = :memberId";
        }
    }

    private String makeWhereClauseForStatus(final ReviewStatus status) {
        if (status.isNone()) {
            return "";
        }
        return " AND rv.status = :status";
    }

    private List<ReviewByRoleData> mapToReviewByRoleDataList(final List<Object[]> result) {
        List<ReviewByRoleData> reviewsByRoleDataList = new ArrayList<>();

        for (Object[] data : result) {
            reviewsByRoleDataList.add(
                    new ReviewByRoleData(
                            (Long) data[0], (String) data[1], (Long) data[2], (ReviewStatus) data[3],
                            (Long) data[4], (String) data[5], (String) data[6]
                    ));
        }
        return reviewsByRoleDataList;
    }
}
