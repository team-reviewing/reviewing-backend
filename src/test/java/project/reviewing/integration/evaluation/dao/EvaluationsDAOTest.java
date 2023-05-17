package project.reviewing.integration.evaluation.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import project.reviewing.evaluation.command.domain.Evaluation;
import project.reviewing.evaluation.query.dao.data.EvaluationForReviewerData;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("EvaluationDAO는 ")
public class EvaluationsDAOTest extends IntegrationTest {

    @DisplayName("특정 리뷰어의 평가 목록을 요청한 page와 size에 맞게 조회한다.")
    @CsvSource({
            "0,1,1,true", "0,2,2,false", "0,3,2,false"
    })
    @ParameterizedTest
    void findEvaluationsForReviewerInPage(final int page, final int size, final int foundSize, final boolean hasNext) {
        // given
        final Member reviewee1 = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
        final Member reviewee2 = createMember(new Member(2L, "J", "J@gmail.com", "imageUrl", "https://github.com/J"));
        final Member reviewerMember = createMemberAndRegisterReviewer(
                new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
        );

        createEvaluation(new Evaluation(reviewerMember.getReviewer().getId(), reviewee1.getId(), 1L, 1.5F, "평가1"));
        createEvaluation(new Evaluation(reviewerMember.getReviewer().getId(), reviewee2.getId(), 2L, 2.0F, "평가2"));

        // when, then
        Slice<EvaluationForReviewerData> result = evaluationsDAO.findEvaluationsByReviewerId(
                reviewerMember.getReviewer().getId(), PageRequest.of(page, size)
        );

        assertAll(
                () -> assertThat(result.hasNext()).isEqualTo(hasNext),
                () -> assertThat(result).size().isEqualTo(foundSize)
        );
    }
}
