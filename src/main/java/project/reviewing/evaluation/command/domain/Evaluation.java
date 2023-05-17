package project.reviewing.evaluation.command.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private Long revieweeId;

    @Column(nullable = false, unique = true)
    private Long reviewId;

    @Column(nullable = false)
    private Float score;

    @Column(nullable = false, length = 100)
    private String content;

    public Evaluation(
            final Long reviewerId, final Long revieweeId, final Long reviewId, final Float score, final String content
    ) {
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.reviewId = reviewId;
        this.score = score;
        this.content = content;
    }
}
