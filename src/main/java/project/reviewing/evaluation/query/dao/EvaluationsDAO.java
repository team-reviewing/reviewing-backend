package project.reviewing.evaluation.query.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import project.reviewing.evaluation.command.domain.Evaluation;
import project.reviewing.evaluation.query.dao.data.EvaluationForReviewerData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class EvaluationsDAO {

    @PersistenceContext
    private final EntityManager em;

    public Slice<EvaluationForReviewerData> findEvaluationsByReviewerId(final Long reviewerId, final Pageable pageable) {
        final String jpql = "SELECT ev, m.username, m.imageUrl "
                + "FROM Evaluation ev "
                + "JOIN Member m ON m.id = ev.revieweeId "
                + "WHERE ev.reviewerId = :reviewerId "
                + "ORDER BY ev.id ASC";
        final Query query = em.createQuery(jpql)
                .setParameter("reviewerId", reviewerId);

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize() + 1);

        final List<Object[]> resultList = query.getResultList();
        return new SliceImpl<>(
                mapToEvaluationForReviewerDataList(findListInPage(resultList, pageable)),
                pageable,
                findHasNext(resultList, pageable)
        );
    }

    private List<EvaluationForReviewerData> mapToEvaluationForReviewerDataList(final List<Object[]> resultList) {
        List<EvaluationForReviewerData> reviewsByRoleDataList = new ArrayList<>();

        for (Object[] data : resultList) {
            Evaluation evaluation = (Evaluation) data[0];

            reviewsByRoleDataList.add(
                    new EvaluationForReviewerData(
                            evaluation.getId(), (String) data[1], (String) data[2],
                            evaluation.getScore(), evaluation.getContent()
                    ));
        }
        return reviewsByRoleDataList;
    }

    private List<Object[]> findListInPage(final List<Object[]> list, final Pageable pageable) {
        return (list.size() > pageable.getPageSize()) ? list.subList(0, pageable.getPageSize()) : list;
    }

    private boolean findHasNext(final List list, final Pageable pageable) {
        return (list.size() > pageable.getPageSize());
    }
}