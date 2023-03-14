package project.reviewing.member.query.dao;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import project.reviewing.member.query.dao.data.MyInformation;

@RequiredArgsConstructor
@Repository
public class MyInformationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<MyInformation> findById(final Long memberId) {
        try {
            final String sql = "SELECT username, email, image_url, profile_url, is_reviewer "
                    + "FROM member "
                    + "WHERE id = :memberId";
            final SqlParameterSource parameterSource = new MapSqlParameterSource("memberId", memberId);

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, parameterSource, rowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<MyInformation> rowMapper() {
        return (rs, rowNum) -> new MyInformation(
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("image_url"),
                rs.getString("profile_url"),
                rs.getBoolean("is_reviewer")
        );
    }
}
