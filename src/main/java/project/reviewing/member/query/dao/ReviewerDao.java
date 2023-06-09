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
import project.reviewing.member.query.dao.data.MyReviewerInformationData;
import project.reviewing.member.query.dao.data.ReviewerWithTagData;
import project.reviewing.member.query.dao.util.ReviewerDataMapper;
import project.reviewing.tag.query.dao.data.TagData;

@RequiredArgsConstructor
@Repository
public class ReviewerDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public boolean existsById(final Long id) {
        final String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reviewer "
                + "WHERE id = :id "
                + ")";
        final SqlParameterSource params = new MapSqlParameterSource("id", id);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    public boolean existByMemberId(final Long memberId) {
        final String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reviewer "
                + "WHERE member_id = :memberId "
                + ")";
        final SqlParameterSource params = new MapSqlParameterSource("memberId", memberId);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    public List<ReviewerData> findById(final Long reviewerId) {
        final String sql = "SELECT r.job, r.career, r.introduction, r.id, r.score, m.username, m.image_url, m.profile_url, t.id tag_id, t.name tag_name "
                + "FROM reviewer r "
                + "JOIN member m ON r.member_id = m.id "
                + "JOIN reviewer_tag rt ON r.id = rt.reviewer_id "
                + "JOIN tag t ON rt.tag_id = t.id "
                + "WHERE r.id = :reviewerId ";
        final SqlParameterSource params = new MapSqlParameterSource("reviewerId", reviewerId);

        return ReviewerDataMapper.map(jdbcTemplate.query(sql, params, rowMapper()));
    }

    public List<MyReviewerInformationData> findByMemberId(final Long memberId) {
        final String sql = "SELECT r.job job, r.career career, r.introduction introduction, t.id tag_id, t.name tag_name "
                + "FROM reviewer r "
                + "JOIN reviewer_tag rt ON r.id = rt.reviewer_id "
                + "JOIN tag t ON rt.tag_id = t.id "
                + "WHERE r.member_id = :memberId";
        final SqlParameterSource params = new MapSqlParameterSource("memberId", memberId);

        return jdbcTemplate.query(sql, params, rowMapperRid());
    }

    public Slice<ReviewerData> findByTag(final Pageable pageable, final Long categoryId, final List<Long> tagIds) {
        final String sql = "SELECT /*! STRAIGHT_JOIN */ r.job, r.career, r.introduction, r.id, r.score, m.username, m.image_url, m.profile_url, t.id tag_id, t.name tag_name "
                + "FROM reviewer r "
                + "JOIN reviewer_tag rt ON r.id = rt.reviewer_id "
                + "JOIN tag t ON rt.tag_id = t.id "
                + "JOIN member m ON r.member_id = m.id "
                + "WHERE r.id IN (" + makeReviewerIdsCond(pageable, categoryId, tagIds) + ")";

        final List<ReviewerData> reviewerData = ReviewerDataMapper.map(jdbcTemplate.query(sql, rowMapper()));
        return new SliceImpl<>(getCurrentPageReviewers(reviewerData, pageable), pageable, hasNext(reviewerData, pageable));
    }

    private String makeReviewerIdsCond(final Pageable pageable, final Long categoryId, final List<Long> tagIds) {
        final String sql = "SELECT /*! STRAIGHT_JOIN */ r.id "
                + "FROM reviewer r "
                + "JOIN reviewer_tag rt ON r.id = rt.reviewer_id "
                + "JOIN tag t ON rt.tag_id = t.id "
                + "JOIN member m ON r.member_id = m.id "
                + "WHERE m.is_reviewer = true AND r.id > :latestId "
                + makeWhereClause(categoryId, tagIds)
                + "GROUP BY r.id "
                + "LIMIT :limit";
        final SqlParameterSource params = new MapSqlParameterSource("limit", pageable.getPageSize() + 1)
                .addValue("latestId", pageable.getPageNumber())
                .addValue("categoryId", categoryId)
                .addValue("tagIds", tagIds);

        List<Long> reviewerIds = jdbcTemplate.query(sql, params,
                (rs, rowNum) -> rs.getLong("id")
        );

        final StringBuilder reviewerIdsCond = new StringBuilder();

        if (!reviewerIds.isEmpty()) {
            reviewerIdsCond.append(reviewerIds.get(0));
        }
        for (int i = 1; i < reviewerIds.size(); i++) {
            reviewerIdsCond.append(",").append(reviewerIds.get(i));
        }
        return reviewerIdsCond.toString();
    }

    private String makeWhereClause(final Long categoryId, final List<Long> tagIds) {
        final String categoryIdCond = "t.category_id = :categoryId ";
        final String tagIdsCond = "rt.tag_id IN (:tagIds) ";

        if (categoryId == null && tagIds == null) {
            return "";
        }
        if (categoryId == null) {
            return "AND " + tagIdsCond;
        }
        if (tagIds == null) {
            return "AND " + categoryIdCond;
        }
        return "AND " + categoryIdCond + "AND " + tagIdsCond;
    }

    private List<ReviewerData> getCurrentPageReviewers(final List<ReviewerData> reviewerData, final Pageable pageable) {
        if (hasNext(reviewerData, pageable)) {
            return reviewerData.subList(0, pageable.getPageSize());
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
                new TagData(rs.getLong("tag_id"), rs.getString("tag_name")),
                rs.getFloat("score")
        );
    }

    private RowMapper<MyReviewerInformationData> rowMapperRid() {
        return (rs, rowNum) -> new MyReviewerInformationData(
                Job.valueOf(rs.getString("job")).getValue(),
                Career.valueOf(rs.getString("career")).getCareer(),
                rs.getString("introduction"),
                new TagData(rs.getLong("tag_id"), rs.getString("tag_name"))
        );
    }
}
