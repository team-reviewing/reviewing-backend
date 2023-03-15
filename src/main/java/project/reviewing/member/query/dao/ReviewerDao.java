package project.reviewing.member.query.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.query.dao.data.ReviewerInformationData;
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

        return jdbcTemplate.query(sql, params, rowMapper());
    }

    private RowMapper<ReviewerInformationData> rowMapper() {
        return (rs, rowNum) -> new ReviewerInformationData(
                Job.valueOf(rs.getString("job")).getValue(),
                Career.valueOf(rs.getString("career")).getCareer(),
                rs.getString("introduction"),
                new TagData(rs.getLong("tag_id"), rs.getString("tag_name"))
        );
    }
}
