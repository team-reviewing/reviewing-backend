package project.reviewing.tag.query.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TagDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<TagData> findByReviewerId(final Long reviewerId) {
        final String sql = "SELECT t.id, t.name "
                + "FROM tag t JOIN reviewer_tag rt ON t.id = rt.tag_id "
                + "WHERE rt.reviewer_id = :reviewerId";
        final SqlParameterSource params = new MapSqlParameterSource("reviewerId", reviewerId);

        return jdbcTemplate.query(sql, params, rowMapper());
    }

    public List<TagData> findAll() {
        final String sql = "SELECT t.id, t.name "
                + "FROM tag t";

        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<TagData> rowMapper() {
        return (rs, rowNum) -> new TagData(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
