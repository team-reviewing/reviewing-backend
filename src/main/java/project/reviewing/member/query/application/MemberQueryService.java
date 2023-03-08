package project.reviewing.member.query.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.query.dao.MyInformation;
import project.reviewing.member.query.dao.MyInformationDao;
import project.reviewing.member.query.response.MyInformationResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberQueryService {

    private final MyInformationDao myInformationDao;

    public MyInformationResponse findMember(final Long memberId) {
        final MyInformation myInformation = myInformationDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MyInformationResponse.of(myInformation);
    }
}
