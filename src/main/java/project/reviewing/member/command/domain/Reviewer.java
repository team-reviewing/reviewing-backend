package project.reviewing.member.command.domain;

import java.util.Set;
import javax.persistence.CollectionTable;
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
    @CollectionTable(name = "reviewer_tag",
            joinColumns = @JoinColumn(name = "tag_id"))
    private Set<Long> techStack;

    private String introduction;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Reviewer(final Job job, final Career career, final Set<Long> techStack, final String introduction) {
        this.job = job;
        this.career = career;
        this.techStack = techStack;
        this.introduction = introduction;
    }

    public void addMember(final Member member) {
        this.member = member;
    }
}
