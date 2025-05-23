package backend.academy.diplom.repositories.auth;

import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.rowmappers.auth.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final NamedParameterJdbcTemplate template;
    private final UserRowMapper userRowMapper;

    public void createUser(User user) {
        String createSql = """
                insert into engineers.user (name, surname, phone_number, email, password, city, status) values
                (:name, :surname, :phoneNumber, :email, :password,  :city,
                :status)""";

        SqlParameterSource parameterSource =
                new MapSqlParameterSource().addValue("name", user.getName())
                        .addValue("surname", user.getSurname())
                        .addValue("phoneNumber", user.getPhoneNumber())
                        .addValue("email", user.getEmail())
                        .addValue("password", user.getPassword())
                        .addValue("city", user.getCity())
                        .addValue("status", user.getStatus());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(createSql, parameterSource, keyHolder, new String[]{"id"});

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    public void updatePassword(User user) {
        String sql = """
                update engineers.user
                set password = :password
                where id = :id""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("password", user.getPassword())
                .addValue("id", user.getId());

        template.update(sql, sqlParameterSource);
    }

    public List<User> getAllUsers() {
        String getUsersQuery = """
                select * from engineers.user;""";

        try {
            return template.query(getUsersQuery, userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    public List<User> findByEmail(String email) {
        String findQuery = """
                select * from engineers.user where email = :email""";

        SqlParameterSource parameterSource = new MapSqlParameterSource("email", email);
        try {
            return template.query(findQuery, parameterSource, userRowMapper);
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<User> findById(long id) {
        String findQuery = """
                select * from engineers.user where id = :id""";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return template.query(findQuery, parameterSource, userRowMapper);
    }

//    public void deleteTokenByUserMail(String email) {
//        String deleteToken = """
//                delete from engineers.refresh_token
//                where user_id = (select id from engineers.user where email
//                = :email)""";
//
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("email", email);
//        template.update(deleteToken, sqlParameterSource);
//    }

//    public void updateStringFields(String email, String lastName, String firstName, String city,
//                                   String gender, LocalDate birthDate) {
//        String updateFields = """
//                update engineers.user u set
//                name = case when :firstName is not null then :firstName else u.name end,
//                surname = case when :lastName is not null then :lastName else u.surname end,
//                birth_date = case when :birthDate is not null then :birthDate else u.birth_date end,
//                gender = case when :gender is not null then :gender else u.gender end,
//                city = case when :city is not null then :city else u.city end
//                where u.email = :email""";
//
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
//                .addValue("firstName", firstName, Types.VARCHAR)
//                .addValue("lastName", lastName, Types.VARCHAR)
//                .addValue("city", city, Types.VARCHAR)
//                .addValue("gender", gender, Types.VARCHAR)
//                .addValue("birthDate", birthDate, Types.DATE)
//                .addValue("email", email, Types.VARCHAR);
//
//        template.update(updateFields, sqlParameterSource);
//    }

    public void updateFileFields(Long userId, String diplomaPath, String photoPath, String resumePath) {
        String updateFields = """
                update engineers.user set
                diploma_path = case when :diplomaPath is not null then :diplomaPath else
                diploma_path end,
                resume_path = case when :resumePath is not null then :resumePath else
                resume_path end,
                photo_path = case when :photoPath is not null then :photoPath else
                photo_path end
                where id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("diplomaPath", diplomaPath, Types.VARCHAR)
                .addValue("resumePath", resumePath, Types.VARCHAR)
                .addValue("photoPath", photoPath, Types.VARCHAR)
                        .addValue("userId", userId);

        template.update(updateFields, sqlParameterSource);
    }

    public void deleteDiploma(Long userId) {
        String deleteDiploma = """
                update engineers.user u set
                diploma_path = null""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteDiploma, sqlParameterSource);
    }

    public void deleteResume(Long userId) {
        String deleteDiploma = """
                update engineers.user u set
                resume_path = null""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteDiploma, sqlParameterSource);
    }

    public void deletePhoto(Long userId) {
        String deleteDiploma = """
                update engineers.user u set
                photo_path = null""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteDiploma, sqlParameterSource);
    }

    public List<User> getAllUsersWhoLikeProject(Long projectId) {
        String sql = """
                select * from engineers.user where id = ( 
                select u.id
                from engineers.project_user_like pul
                join engineers.user u on pul.user_id = u.id
                where pul.project_id = :projectId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);

        return template.query(sql, sqlParameterSource, userRowMapper);
    }


    public void updateAbout(long id, String about) {
        String sql = """
                update engineers.user
                set about = :about
                where id = :id""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("about", about)
                .addValue("id", id);

        template.update(sql, sqlParameterSource);
    }

    public void updateProfileSettingFields(Long userId, String name, String surname, String gender, String city,
                                           Date birthDate, String email, String status, Boolean hideBirthday,
                                           Boolean isPublic) {
        String sql = """
                update engineers.user
                set name = :name,
                surname = :surname,
                gender = :gender,
                city = :city,
                birth_date = :birthDate,
                email = :email,
                status = :status,
                hide_birthday = :hideBirthday,
                is_public = :isPublic
                where id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("surname", surname)
                .addValue("gender", gender)
                .addValue("city", city)
                .addValue("birthDate", birthDate)
                .addValue("email", email)
                .addValue("status", status)
                .addValue("hideBirthday", hideBirthday)
                .addValue("isPublic", isPublic)
                        .addValue("userId", userId);

        template.update(sql, sqlParameterSource);
    }
}
