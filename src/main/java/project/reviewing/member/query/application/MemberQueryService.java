package project.reviewing.member.query.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.query.application.response.MyInformationResponse;
import project.reviewing.member.query.application.response.ReviewerInformationResponse;
import project.reviewing.member.query.dao.MyInformationDao;
import project.reviewing.member.query.dao.ReviewerDao;
import project.reviewing.member.query.dao.data.MyInformation;
import project.reviewing.member.query.dao.data.ReviewerInformationData;
import project.reviewing.tag.command.domain.TagRepository;
import project.reviewing.tag.query.application.response.TagResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberQueryService {

    private final MyInformationDao myInformationDao;
    private final ReviewerDao reviewerDao;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    public MyInformationResponse findMember(final Long memberId) {
        final MyInformation myInformation = myInformationDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MyInformationResponse.of(myInformation);
    }

    public ReviewerInformationResponse findReviewerWithChoiceList(final Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        if (!reviewerDao.existByMemberId(memberId)) {
            return ReviewerInformationResponse.empty();
        }
        final List<ReviewerInformationData> reviewerInformationData = reviewerDao.findByMemberId(memberId);

        return ReviewerInformationResponse.of(
                reviewerInformationData,
                Arrays.stream(Job.values()).map(Job::getValue).collect(Collectors.toList()),
                Arrays.stream(Career.values()).map(Career::getCareer).collect(Collectors.toList()),
                tagRepository.findAll().stream().map(TagResponse::from).collect(Collectors.toList())
        );
    }
}
