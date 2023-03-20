package project.reviewing.member.query.dao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import project.reviewing.member.query.dao.data.ReviewerData;
import project.reviewing.member.query.dao.data.ReviewerWithTagData;
import project.reviewing.tag.query.dao.data.TagData;

public class ReviewerDataMapper {

    public static List<ReviewerData> map(final List<ReviewerWithTagData> reviewerWithTagData) {
        final Set<Long> reviewerIds = reviewerWithTagData.stream()
                .map(ReviewerWithTagData::getId)
                .collect(Collectors.toSet());

        final List<ReviewerData> reviewerData = new ArrayList<>();

        for (final Long id : reviewerIds) {
            final List<TagData> tagData = reviewerWithTagData.stream()
                    .filter(data -> Objects.equals(data.getId(), id))
                    .map(ReviewerWithTagData::getTagData)
                    .collect(Collectors.toList());
            final ReviewerWithTagData data = reviewerWithTagData.stream()
                    .filter(d -> Objects.equals(d.getId(), id))
                    .findFirst()
                    .get();
            reviewerData.add(new ReviewerData(
                    data.getId(), data.getJob(), data.getCareer(), data.getIntroduction(),
                    data.getUsername(), data.getImageUrl(), data.getProfileUrl(), tagData
            ));
        }

        return reviewerData;
    }
}
