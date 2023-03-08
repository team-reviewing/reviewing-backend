package project.reviewing.integration.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.member.query.dao.MyInformation;
import project.reviewing.member.query.dao.MyInformationDao;
import project.reviewing.member.query.response.MyInformationResponse;

@DisplayName("MemberQueryService 는")
@DataJpaTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class MemberQueryServiceTest {

    @Autowired
    private MyInformationDao myInformationDao;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("내 정보 조회 시 ")
    @Nested
    class MemberFindTest {

        @DisplayName("정상적인 경우 회원을 조회한다.")
        @Test
        void findMember() {
            final MemberQueryService sut = new MemberQueryService(myInformationDao);
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png");
            final Long memberId = createMember(member).getId();

            final MyInformationResponse actual = sut.findMember(memberId);

            assertThat(actual).usingRecursiveComparison().isEqualTo(toMyInformationResponse(member));
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void findNotExistMember() {
            final MemberQueryService sut = new MemberQueryService(myInformationDao);
            final Long notExistMemberId = 1L;

            assertThatThrownBy(() -> sut.findMember(notExistMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    private Member createMember(final Member member) {
        return memberRepository.save(member);
    }

    private MyInformationResponse toMyInformationResponse(final Member member) {
        return MyInformationResponse.of(toMyInformation(member));
    }

    private MyInformation toMyInformation(final Member member) {
        return new MyInformation(
                member.getUsername(), member.getEmail(),
                member.getImageUrl(), member.getProfileUrl(),
                member.isReviewer()
        );
    }
}
