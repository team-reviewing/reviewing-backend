package project.reviewing.tag.query.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import project.reviewing.tag.query.dao.data.TagData;
import project.reviewing.tag.query.dao.data.TagWithCategoryData;

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

    public List<TagWithCategoryData> findAll() {
        final String sql = "SELECT c.id category_id, c.name category_name, t.id tag_id, t.name tag_name "
                + "FROM tag t "
                + "JOIN category c ON t.category_id = c.id";

        return jdbcTemplate.query(sql, rowMapperTwc());
    }

    private RowMapper<TagData> rowMapper() {
        return (rs, rowNum) -> new TagData(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    private RowMapper<TagWithCategoryData> rowMapperTwc() {
        return (rs, rowNum) -> new TagWithCategoryData(
                rs.getLong("category_id"),
                rs.getString("category_name"),
                rs.getLong("tag_id"),
                rs.getString("tag_name")
        );
    }
}
