package project.reviewing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.member.domain.Member;

@DisplayName("Member 는")
public class MemberTest {

    @DisplayName("username과 email을 수정할 수 있다.")
    @Test
    void updateUsernameAndEmail() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png");
        final Member updatedMember = new Member(1L, "newUsername", "newEmail@gmail.com", "image.png");

        sut.update(updatedMember);

        assertAll(
                () -> assertThat(sut.getUsername()).isEqualTo("newUsername"),
                () -> assertThat(sut.getEmail()).isEqualTo("newEmail@gmail.com")
        );
    }
}
