package backend.academy.diplom.repositories.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSectionAndStampRepository {

    private final NamedParameterJdbcTemplate template;

    public void addUserSection(Long userId, String section) {
        String addUserSection = """
                insert into engineers.user_section_and_stamp(user_id, section_and_stamp_id)
                values (:userId, (select id from engineers.section_and_stamp where name = :section))""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                        .addValue("section", section);

        template.update(addUserSection, sqlParameterSource);
    }

    public void deleteByUserId(Long userId) {
        String deleteSql = """
                delete from engineers.user_section_and_stamp
                where user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteSql, sqlParameterSource);
    }
}
