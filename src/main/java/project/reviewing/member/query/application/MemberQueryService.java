package project.reviewing.member.query.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.query.application.response.MyInformationResponse;
import project.reviewing.member.query.application.response.MyReviewerInformationResponse;
import project.reviewing.member.query.application.response.ReviewersResponse;
import project.reviewing.member.query.dao.MyInformationDao;
import project.reviewing.member.query.dao.ReviewerDao;
import project.reviewing.member.query.dao.data.MyInformationData;
import project.reviewing.member.query.dao.data.MyReviewerInformationData;
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
        final MyInformationData myInformationData = myInformationDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MyInformationResponse.of(myInformationData);
    }

    public MyReviewerInformationResponse findReviewerWithChoiceList(final Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        if (!reviewerDao.existByMemberId(memberId)) {
            return MyReviewerInformationResponse.empty();
        }
        final List<MyReviewerInformationData> myReviewerInformationData = reviewerDao.findByMemberId(memberId);

        return MyReviewerInformationResponse.of(
                myReviewerInformationData,
                Arrays.stream(Job.values()).map(Job::getValue).collect(Collectors.toList()),
                Arrays.stream(Career.values()).map(Career::getCareer).collect(Collectors.toList()),
                tagRepository.findAll().stream().map(TagResponse::from).collect(Collectors.toList())
        );
    }

    public ReviewersResponse findReviewers(final Pageable pageable, final Long categoryId, final List<Long> tagIds) {
        return ReviewersResponse.from(reviewerDao.findByTag(pageable, categoryId, tagIds));
    }
}
