package backend.academy.diplom.repositories.profile;

import backend.academy.diplom.repositories.rowmappers.GeneralTagsRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SocialNetworkRepository {

    private final NamedParameterJdbcTemplate template;
    private final GeneralTagsRowMapper generalTagsRowMapper;

    public List<String> getSocialNetworksByUserId(Long userId) {
        String sql = """
                select s.name from engineers."user" u
                join engineers.user_social_network un on u.id = un.user_id
                join engineers.social_network s on un.social_network_id = s.id
                where u.id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(sql, sqlParameterSource, generalTagsRowMapper);
    }
}
