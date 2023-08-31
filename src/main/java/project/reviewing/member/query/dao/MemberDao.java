package project.reviewing.member.query.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberDao {

    @PersistenceContext
    private final EntityManager em;

    public Optional<Member> findByReviewerIdBySlockOnReviewer(final long reviewerId) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.lock.timeout", 3000L);
        Reviewer reviewer = em.find(Reviewer.class, reviewerId, LockModeType.PESSIMISTIC_READ, properties);
        return (reviewer != null) ? Optional.of(reviewer.getMember()) : Optional.empty();
    }

    public Optional<Member> findByReviewerIdByXlockOnReviewer(final long reviewerId) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.lock.timeout", 3000L);
        Reviewer reviewer = em.find(Reviewer.class, reviewerId, LockModeType.PESSIMISTIC_WRITE, properties);
        return (reviewer != null) ? Optional.of(reviewer.getMember()) : Optional.empty();
    }
}
