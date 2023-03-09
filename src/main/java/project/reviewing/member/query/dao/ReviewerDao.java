package project.reviewing.member.query.dao;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.query.dao.data.ReviewerData;

@RequiredArgsConstructor
@Repository
public class ReviewerDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<ReviewerData> findByMemberId(final Long memberId) {
        try {
            final String sql = "SELECT job, career, introduction "
                    + "FROM reviewer "
                    + "WHERE member_id = :memberId";
            final SqlParameterSource params = new MapSqlParameterSource("memberId", memberId);

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, rowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<ReviewerData> rowMapper() {
        return (rs, rowNum) -> new ReviewerData(
                Job.valueOf(rs.getString("job")).getValue(),
                Career.valueOf(rs.getString("career")).getCareer(),
                rs.getString("introduction")
        );
    }
}
