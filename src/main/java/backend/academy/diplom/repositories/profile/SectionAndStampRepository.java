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
public class SectionAndStampRepository {
    private final NamedParameterJdbcTemplate template;
    private final GeneralTagsRowMapper generalTagsRowMapper;

    public List<String> getSectionAndStampByUserid(Long userId) {
        String sql = """
                select s.name from engineers."user" u
                join engineers.user_section_and_stamp un on u.id = un.user_id
                join engineers.section_and_stamp s on un.section_and_stamp_id = s.id
                where u.id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(sql, sqlParameterSource, generalTagsRowMapper);
    }

    public List<String> getSectionAndStampByProjectId(Long projectId) {
        String sql = """
                select sas.name from engineers.project p
                join engineers.project_section_and_stamp psas on p.id = psas.project_id
                join engineers.section_and_stamp sas on psas.section_and_stamp_id = sas.id
                where project_id = :projectId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);

        return template.query(sql, sqlParameterSource, generalTagsRowMapper);
    }
}
