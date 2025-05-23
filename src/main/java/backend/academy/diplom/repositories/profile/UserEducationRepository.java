package backend.academy.diplom.repositories.profile;

import backend.academy.diplom.entities.user.UserEducation;
import backend.academy.diplom.repositories.rowmappers.EducationRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserEducationRepository {
    private final NamedParameterJdbcTemplate template;
    private final EducationRowMapper educationRowMapper;

    public void addEducation(Long userId, String education) {
        String addSocialNetwork = """
                insert into engineers.user_education(user_id, education)
                values (:userId, :education)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("education", education);

        try {
            template.update(addSocialNetwork, sqlParameterSource);
        } catch (Exception e) {
            log.info("Анлакич");
        }
    }

    public void deleteEducation(Long userId) {
        String deleteSql = """
                delete from engineers.user_education
                where user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteSql, sqlParameterSource);
    }

    public List<String> getEducationByUserId(Long userId) {
        String getEducation = """
                select * from engineers.user_education
                where user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(getEducation, sqlParameterSource, educationRowMapper).stream().map(
                UserEducation::getEducation
        ).toList();
    }
}
