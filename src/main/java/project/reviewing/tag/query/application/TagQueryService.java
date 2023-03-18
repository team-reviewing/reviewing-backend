package project.reviewing.tag.query.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.tag.query.application.response.CategoriesResponse;
import project.reviewing.tag.query.dao.TagDao;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagQueryService {

    private final TagDao tagDao;

    public CategoriesResponse findTags() {
        return CategoriesResponse.from(tagDao.findAll());
    }
}
