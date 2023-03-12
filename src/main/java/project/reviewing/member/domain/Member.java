package project.reviewing.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "github_id", nullable = false, unique = true)
    private Long githubId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "image_url")
    private String imageURL;

    //@Column(name = "github_url", nullable = false, unique = true)
    private String githubURL;

    private String introduction;

    public void updateGithubURL(final String githubURL) {
        this.githubURL = githubURL;
    }
}
