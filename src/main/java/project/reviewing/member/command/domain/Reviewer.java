package project.reviewing.member.command.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Job job;

    @Enumerated(EnumType.STRING)
    private Career career;

    @ElementCollection
    @CollectionTable(name = "reviewer_tag", joinColumns = @JoinColumn(name = "reviewer_id"))
    @Column(name = "tag_id", nullable = false)
    private Set<Long> techStack = new HashSet<>();

    private String introduction;

    @Column(nullable = false)
    private Float score;

    @Column(nullable = false)
    private Long evaluationCnt;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Reviewer(final Job job, final Career career, final Set<Long> techStack, final String introduction) {
        this.job = job;
        this.career = career;
        this.techStack = techStack;
        this.introduction = introduction;
        score = 0.0F;
        evaluationCnt = 0L;
    }

    public void addMember(final Member member) {
        this.member = member;
    }

    public void update(final Reviewer reviewer) {
        this.job = reviewer.getJob();
        this.career = reviewer.getCareer();
        this.techStack = reviewer.getTechStack();
        this.introduction = reviewer.getIntroduction();
    }
}
