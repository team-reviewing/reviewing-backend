package project.reviewing.member.query.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.query.dao.data.ReviewerData;
import project.reviewing.member.query.dao.data.ReviewerInformationData;
import project.reviewing.member.query.dao.data.ReviewerWithTagData;
import project.reviewing.member.query.dao.util.ReviewerDataMapper;
import project.reviewing.tag.query.dao.data.TagData;

@RequiredArgsConstructor
@Repository
public class ReviewerDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public boolean existByMemberId(final Long memberId) {
        final String sql = "SELECT EXISTS ( "
                + "SELECT 1 "
                + "FROM reviewer "
                + "WHERE member_id = :memberId "
                + ")";
        final SqlParameterSource params = new MapSqlParameterSource("memberId", memberId);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    public List<ReviewerInformationData> findByMemberId(final Long memberId) {
        final String sql = "SELECT r.job job, r.career career, r.introduction introduction, t.id tag_id, t.name tag_name "
                + "FROM reviewer r "
                + "JOIN reviewer_tag rt ON r.id = rt.reviewer_id "
                + "JOIN tag t ON rt.tag_id = t.id "
                + "WHERE r.member_id = :memberId";
        final SqlParameterSource params = new MapSqlParameterSource("memberId", memberId);

        return jdbcTemplate.query(sql, params, rowMapperRid());
    }

    public Slice<ReviewerData> findByTag(final Pageable pageable) {
        final String sql = "SELECT r.id, r.job, r.career, r.introduction, m.username, m.image_url, m.profile_url, t.id tag_id, t.name tag_name "
                + "FROM reviewer r "
                + "JOIN member m ON r.member_id = m.id "
                + "JOIN reviewer_tag rt ON r.id = rt.reviewer_id "
                + "JOIN tag t ON rt.tag_id = t.id "
                + "LIMIT :limit OFFSET :offset";
        final SqlParameterSource params = new MapSqlParameterSource("limit", pageable.getPageSize() + 1)
                .addValue("offset", pageable.getOffset());

        final List<ReviewerData> reviewerData = ReviewerDataMapper.map(jdbcTemplate.query(sql, params, rowMapper()));
        return new SliceImpl<>(getCurrentPageReviewers(reviewerData, pageable), pageable, hasNext(reviewerData, pageable));
    }

    private List<ReviewerData> getCurrentPageReviewers(final List<ReviewerData> reviewerData, final Pageable pageable) {
        if (hasNext(reviewerData, pageable)) {
            return reviewerData.subList(0, reviewerData.size() - 1);
        }
        return reviewerData;
    }

    private boolean hasNext(final List<ReviewerData> reviewerData, final Pageable pageable) {
        return reviewerData.size() > pageable.getPageSize();
    }

    private RowMapper<ReviewerWithTagData> rowMapper() {
        return (rs, rowNum) -> new ReviewerWithTagData(
                rs.getLong("id"),
                Job.valueOf(rs.getString("job")).getValue(),
                Career.valueOf(rs.getString("career")).getCareer(),
                rs.getString("introduction"),
                rs.getString("username"),
                rs.getString("image_url"),
                rs.getString("profile_url"),
                new TagData(rs.getLong("tag_id"), rs.getString("tag_name"))
        );
    }

    private RowMapper<ReviewerInformationData> rowMapperRid() {
        return (rs, rowNum) -> new ReviewerInformationData(
                Job.valueOf(rs.getString("job")).getValue(),
                Career.valueOf(rs.getString("career")).getCareer(),
                rs.getString("introduction"),
                new TagData(rs.getLong("tag_id"), rs.getString("tag_name"))
        );
    }
}
