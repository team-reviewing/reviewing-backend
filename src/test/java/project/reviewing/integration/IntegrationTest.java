package project.reviewing.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.query.dao.MyInformationDao;
import project.reviewing.member.query.dao.ReviewerDao;
import project.reviewing.tag.query.dao.TagDao;

@DataJpaTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class IntegrationTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected MyInformationDao myInformationDao;

    @Autowired
    protected ReviewerDao reviewerDao;

    @Autowired
    protected TagDao tagDao;

    @Autowired
    protected TestEntityManager entityManager;
}
