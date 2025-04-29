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
public class SoftwareSkillRepository {

    private final NamedParameterJdbcTemplate template;
    private final GeneralTagsRowMapper generalTagsRowMapper;

    public List<String> getSoftwareSkillByUserId(Long userId) {
        String sql = """
                select s.name from engineers."user" u
                join engineers.user_software_skill un on u.id = un.user_id
                join engineers.software_skill s on un.software_skill_id = s.id
                where u.id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(sql, sqlParameterSource, generalTagsRowMapper);
    }

    public List<String> getSoftwareSkillByProjectId(Long projectId) {
        String sql = """
                select ss.name from engineers.project p
                join engineers.project_software_skill pss on p.id = pss.project_id
                join engineers.software_skill ss on ss.id = pss.software_skill_id
                where project_id = :projectId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);

        return template.query(sql, sqlParameterSource, generalTagsRowMapper);
    }
}
