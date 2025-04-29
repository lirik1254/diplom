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
public class UserSoftwareSkillRepository {

    private final NamedParameterJdbcTemplate template;
    private final GeneralTagsRowMapper generalTagsRowMapper;

    public void addSoftwareSkill(Long userId, String softwareSkill) {
        String addUserSection = """
                insert into engineers.user_software_skill(user_id, software_skill_id)
                values (:userId, (select id from engineers.software_skill where name = :softwareSkill))""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("softwareSkill", softwareSkill);

        template.update(addUserSection, sqlParameterSource);
    }

    public void deleteByUserId(Long userId) {
        String deleteSql = """
                delete from engineers.user_software_skill
                where user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteSql, sqlParameterSource);
    }

    public List<String> getSoftwareSkillsByUserId(Long userId) {
        String sql = """
                select ss.name from engineers."user" u
                join engineers.user_software_skill un on u.id = un.user_id
                join engineers.software_skill ss on un.software_skill_id = ss.id
                where u.id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(sql, sqlParameterSource, generalTagsRowMapper);
    }
}
