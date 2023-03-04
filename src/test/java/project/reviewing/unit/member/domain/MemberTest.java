package project.reviewing.unit.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.domain.Member;
import project.reviewing.member.exception.InvalidMemberException;

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

    @DisplayName("기존과 동일한 username으로 수정할 수 없다.")
    @Test
    void updateSameUsername() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png");
        final Member updatedMember = new Member(1L, "username", "newEmail@gmail.com", "image.png");

        assertThatThrownBy(() -> sut.update(updatedMember))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage(ErrorType.SAME_USERNAME_AS_BEFORE.getMessage());
    }

    @DisplayName("기존과 동일한 email로 수정할 수 없다.")
    @Test
    void updateSameEmail() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png");
        final Member updatedMember = new Member(1L, "newUsername", "email@gmail.com", "image.png");

        assertThatThrownBy(() -> sut.update(updatedMember))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage(ErrorType.SAME_EMAIL_AS_BEFORE.getMessage());
    }
}