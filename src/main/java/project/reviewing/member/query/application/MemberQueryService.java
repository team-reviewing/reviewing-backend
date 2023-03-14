package project.reviewing.member.query.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.exception.ReviewerNotFoundException;
import project.reviewing.member.query.application.response.MyInformationResponse;
import project.reviewing.member.query.application.response.ReviewerInformationResponse;
import project.reviewing.member.query.dao.MyInformationDao;
import project.reviewing.member.query.dao.ReviewerDao;
import project.reviewing.member.query.dao.data.MyInformation;
import project.reviewing.member.query.dao.data.ReviewerData;
import project.reviewing.tag.query.application.response.TagResponse;
import project.reviewing.tag.command.domain.TagRepository;
import project.reviewing.tag.query.dao.TagDao;
import project.reviewing.tag.query.dao.data.TagData;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberQueryService {

    private final MyInformationDao myInformationDao;
    private final ReviewerDao reviewerDao;
    private final TagDao tagDao;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    public MyInformationResponse findMember(final Long memberId) {
        final MyInformation myInformation = myInformationDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MyInformationResponse.of(myInformation);
    }

    public ReviewerInformationResponse findReviewerWithChoiceList(final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        if (!reviewerDao.existByMemberId(memberId)) {
            return ReviewerInformationResponse.empty();
        }
        final ReviewerData reviewerData = reviewerDao.findByMemberId(memberId)
                .orElseThrow(ReviewerNotFoundException::new);
        final List<TagData> techStack = tagDao.findByReviewerId(member.getReviewer().getId());

        final ReviewerInformationResponse response = ReviewerInformationResponse.from(reviewerData, techStack);
        response.addChoiceList(
                Arrays.stream(Job.values()).map(Job::getValue).collect(Collectors.toList()),
                Arrays.stream(Career.values()).map(Career::getCareer).collect(Collectors.toList()),
                tagRepository.findAll().stream().map(TagResponse::from).collect(Collectors.toList())
        );
        return response;
    }
}
