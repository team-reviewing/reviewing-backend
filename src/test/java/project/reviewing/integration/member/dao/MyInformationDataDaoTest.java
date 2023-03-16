package project.reviewing.integration.member.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.query.dao.data.MyInformationData;

@DisplayName("MemberDao 는")
public class MyInformationDataDaoTest extends IntegrationTest {

    @DisplayName("내 정보 조회 시")
    @Nested
    class myInformationTest {

        @DisplayName("회원이 존재하면 조회 결과를 반환한다.")
        @Test
        void findMember() {
            final Member member = createMember(new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile"));

            final MyInformationData actual = myInformationDao.findById(member.getId()).get();

            assertThat(actual).usingRecursiveComparison().isEqualTo(toMyInformation(member));
        }

        @DisplayName("회원이 존재하지 않으면 빈 값을 반환한다.")
        @Test
        void findNotExistMember() {
            final Long wrongMemberId = 1L;

            final Optional<MyInformationData> actual = myInformationDao.findById(wrongMemberId);

            assertThat(actual).isEqualTo(Optional.empty());
        }
    }

    private MyInformationData toMyInformation(final Member member) {
        return new MyInformationData(
                member.getUsername(), member.getEmail(),
                member.getImageUrl(), member.getProfileUrl(),
                member.isReviewer()
        );
    }
}
