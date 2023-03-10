package project.reviewing.unit.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.exception.InvalidMemberException;

@DisplayName("Member 는")
public class MemberTest {

    @DisplayName("username과 email을 수정할 수 있다.")
    @Test
    void updateUsernameAndEmail() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
        final Member updatedMember = new Member(1L, "newUsername", "newEmail@gmail.com", "image.png", "github.com/profile");

        sut.update(updatedMember);

        assertAll(
                () -> assertThat(sut.getUsername()).isEqualTo("newUsername"),
                () -> assertThat(sut.getEmail()).isEqualTo("newEmail@gmail.com")
        );
    }

    @DisplayName("기존과 동일한 username으로 수정할 수 없다.")
    @Test
    void updateSameUsername() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
        final Member updatedMember = new Member(1L, "username", "newEmail@gmail.com", "image.png", "github.com/profile");

        assertThatThrownBy(() -> sut.update(updatedMember))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage(ErrorType.SAME_USERNAME_AS_BEFORE.getMessage());
    }

    @DisplayName("기존과 동일한 email로 수정할 수 없다.")
    @Test
    void updateSameEmail() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
        final Member updatedMember = new Member(1L, "newUsername", "email@gmail.com", "image.png", "github.com/profile");

        assertThatThrownBy(() -> sut.update(updatedMember))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage(ErrorType.SAME_EMAIL_AS_BEFORE.getMessage());
    }

    @DisplayName("리뷰어를 등록하면 리뷰어 상태가 활성화된다.")
    @Test
    void onReviewer() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
        final Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L, 2L), "안녕하세요");

        sut.register(reviewer);

        assertThat(sut.isReviewer()).isTrue();
    }

    @DisplayName("이미 리뷰어 등록이 된 상태에서 리뷰어 등록을 할 수 없다.")
    @Test
    void registeredReviewer() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
        final Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L, 2L), "안녕하세요");
        sut.register(reviewer);

        assertThatThrownBy(() -> sut.register(reviewer))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage(ErrorType.ALREADY_REGISTERED.getMessage());
    }

    @DisplayName("리뷰어 등록을 하기 전에 활성화 상태로 변경할 수 없다.")
    @Test
    void notChangeStatus() {
        final Member sut = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");

        assertThatThrownBy(sut::changeReviewerStatus)
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage(ErrorType.DO_NOT_REGISTERED.getMessage());
    }
}
