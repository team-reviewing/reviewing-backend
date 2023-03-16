package project.reviewing.tag.query.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import project.reviewing.tag.query.dao.data.TagWithCategoryData;

@RequiredArgsConstructor
@Repository
public class TagDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<TagWithCategoryData> findAll() {
        final String sql = "SELECT c.id category_id, c.name category_name, t.id tag_id, t.name tag_name "
                + "FROM tag t "
                + "JOIN category c ON t.category_id = c.id";

        return jdbcTemplate.query(sql, rowMapperTwc());
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
