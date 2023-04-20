package project.reviewing.review.query.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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

    public List<ReviewByRoleData> findReviewsByReviewer(final Long memberId) {
        final String sql = "SELECT rv.id, rv.title, rv.reviewerId, m.id, m.username, m.imageUrl "
                + "FROM Member m1 "
                + "JOIN m1.reviewer rr "
                + "JOIN Review rv ON rv.reviewerId = rr.id "
                + "JOIN Member m ON m.id = rv.revieweeId "
                + "WHERE m1.id = :memberId";

        Query query = em.createQuery(sql);
        query.setParameter("memberId", memberId);

        return mapToReviewByRoleDataList(query.getResultList());
    }

    public List<ReviewByRoleData> findReviewsByReviewee(final Long memberId) {
        final String sql = "SELECT rv.id, rv.title, rv.reviewerId, m.id, m.username, m.imageUrl "
                + "FROM Member m "
                + "JOIN m.reviewer rr "
                + "JOIN Review rv ON rv.reviewerId = rr.id "
                + "WHERE rv.revieweeId = :memberId";

        Query query = em.createQuery(sql);
        query.setParameter("memberId", memberId);

        return mapToReviewByRoleDataList(query.getResultList());
    }

    private List<ReviewByRoleData> mapToReviewByRoleDataList(final List<Object[]> result) {
        List<ReviewByRoleData> reviewsByRoleDataList = new ArrayList<>();

        for (Object[] data : result) {
            reviewsByRoleDataList.add(
                    new ReviewByRoleData(
                            (Long) data[0], (String) data[1], (Long) data[2],
                            (Long) data[3], (String) data[4], (String) data[5]
                    ));
        }
        return reviewsByRoleDataList;
    }
}
