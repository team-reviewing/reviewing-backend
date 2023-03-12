package project.reviewing.member.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long githubId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String imageUrl;

    @Column(nullable = false, unique = true)
    private String githubUrl;

    private String introduction;

    public Member(
            final Long githubId, final String username, final String email,
            final String imageUrl, final String githubUrl, final String introduction
    ) {
        this.githubId = githubId;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.githubUrl = githubUrl;
        this.introduction = introduction;
    }

    public void updateGithubURL(final String githubUrl) {
        this.githubUrl = githubUrl;
    }
}
