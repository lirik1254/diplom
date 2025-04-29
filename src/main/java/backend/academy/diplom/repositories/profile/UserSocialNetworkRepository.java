package backend.academy.diplom.repositories.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserSocialNetworkRepository {

    private final NamedParameterJdbcTemplate template;

    public void addSocialNetwork(Long userId, String socialNetwork) {
        String addUserSection = """
                INSERT INTO engineers.social_network(name)
                SELECT :socialNetwork
                WHERE NOT EXISTS (
                    SELECT 1 FROM engineers.social_network WHERE name = :socialNetwork
                );
                
                insert into engineers.user_social_network(user_id, social_network_id)
                values (:userId, (select id from engineers.social_network where name = :socialNetwork))""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("socialNetwork", socialNetwork);

        try {
            template.update(addUserSection, sqlParameterSource);
        } catch (Exception e) {
            log.info("Попытка одинаковой вставки соц сетей");
        }
    }

    public void deleteByUserId(Long userId) {
        String deleteSql = """
                WITH deleted AS (
                  DELETE FROM engineers.user_social_network
                  WHERE user_id = :userId
                  RETURNING social_network_id
                )
                DELETE FROM engineers.social_network
                WHERE id IN (SELECT social_network_id FROM deleted);""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteSql, sqlParameterSource);
    }

}
