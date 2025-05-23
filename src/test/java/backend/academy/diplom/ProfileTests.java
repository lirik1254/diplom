package backend.academy.diplom;

import backend.academy.diplom.DTO.profile.ProfileSettingInfoDTO;
import backend.academy.diplom.DTO.profile.UploadProfileDTO;
import backend.academy.diplom.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileTests extends TestBase {

    @MockitoBean
    private FileService fileService;

    private void initProfileInfo() {
        when(fileService.getPresignedLink(anyString())).thenReturn("presignedLink");

        String sql = """
            UPDATE engineers.user
            SET name = ?,
                surname = ?,
                birth_date = ?,
                city = ?,
                status = ?,
                photo_path = ?,
                resume_path = ?,
                diploma_path = ?,
                telegram = ?,
                about = ?,
                gender = ?
            WHERE id = ?
    """;
        List<Object[]> batchArgs = Collections.singletonList(
                new Object[]{"Кирилл", "Шульжик", Date.valueOf("2003-09-17"), "Пермь", "Ищу работу", "photoPath",
                "resumePath", "diplomaPath", "telegram", "about", "Мужской", 1}
        );

        jdbcTemplate.batchUpdate(sql, batchArgs);
        initEducation();
        initSocialNetwork();
        initUserSectionAndStamps();
        initUserSoftwareSkill();
    }

    private void initUserSectionAndStamps() {
        String sql = """
                insert into engineers.user_section_and_stamp(user_id, section_and_stamp_id)
                values (1,1),
                (1, 2)""";

        jdbcTemplate.update(sql);
    }

    private void initUserSoftwareSkill() {
        String sql = """
                insert into engineers.user_software_skill(user_id, software_skill_id)
                values (1, 1),
                (1, 2)""";

        jdbcTemplate.update(sql);
    }

    private void initEducation() {
        String sql = """
                insert into engineers.user_education(id, user_id, education)
                values (1, 1, 'Академия бекенда'),
                (2, 1, 'НИУ ВШЭ')""";

        jdbcTemplate.update(sql);
    }

    private void initSocialNetwork() {
        String sql = """
                insert into engineers.user_social_network(id, user_id, social_network)
                values (1, 1, 'vk.com/kirik228'),
                (2, 1, 'odnoklassniki/52')""";

        jdbcTemplate.update(sql);
    }


    @Test
    public void getProfilePreview() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initProfileInfo();

        mockMvc.perform(get("/api/profile/profile-preview")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Кирилл Шульжик"))
                .andExpect(jsonPath("$.age").value(21))
                .andExpect(jsonPath("$.city").value("Пермь"))
                .andExpect(jsonPath("$.status").value("Ищу работу"))
                .andExpect(jsonPath("$.photoPath").value("presignedLink"));
    }

    @Test
    public void getProfileInfo() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initProfileInfo();

        mockMvc.perform(get("/api/profile/profile-info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sectionAndStamps[0]").value("ГП"))
                .andExpect(jsonPath("$.sectionAndStamps[1]").value("АС"))
                .andExpect(jsonPath("$.softwareSkills[0]").value("Allplan"))
                .andExpect(jsonPath("$.softwareSkills[1]").value("Renga"))
                .andExpect(jsonPath("$.resumePath").value("presignedLink"))
                .andExpect(jsonPath("$.resumeName").value("resumePath"))
                .andExpect(jsonPath("$.education[0]").value("Академия бекенда"))
                .andExpect(jsonPath("$.education[1]").value("НИУ ВШЭ"))
                .andExpect(jsonPath("$.diplomaPath").value("presignedLink"))
                .andExpect(jsonPath("$.diplomaName").value("diplomaPath"))
                .andExpect(jsonPath("$.socialNetworks[0]").value("vk.com/kirik228"))
                .andExpect(jsonPath("$.socialNetworks[1]").value("odnoklassniki/52"))
                .andExpect(jsonPath("$.telegram").value("telegram"))
                .andExpect(jsonPath("$.about").value("about"));
    }

    @Test
    public void getProfileSettingInfo() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initProfileInfo();

        mockMvc.perform(get("/api/profile/profile-setting-info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoPath").value("presignedLink"))
                .andExpect(jsonPath("$.surname").value("Шульжик"))
                .andExpect(jsonPath("$.name").value("Кирилл"))
                .andExpect(jsonPath("$.gender").value("Мужской"))
                .andExpect(jsonPath("$.city").value("Пермь"))
                .andExpect(jsonPath("$.birthday").value("2003-09-17"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.status").value("Ищу работу"))
                .andExpect(jsonPath("$.isHideBirthday").value(false))
                .andExpect(jsonPath("$.isPublic").value(false));
    }

    @Test
    public void uploadProfileInfo() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initProfileInfo();

        UploadProfileDTO uploadProfileDTO = new UploadProfileDTO(
                List.of("АР", "ВК"),
                List.of("Allplan", "Renga"),
                List.of("академия бекенда", "вшэ"),
                List.of("вк ком", "одноклассники ком"),
                "Я кирюха"
        );

        mockMvc.perform(post("/api/profile/upload-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uploadProfileDTO)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/profile/profile-info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(jsonPath("$.sectionAndStamps[0]").value("АР"))
                .andExpect(jsonPath("$.sectionAndStamps[1]").value("ВК"))
                .andExpect(jsonPath("$.softwareSkills[0]").value("Allplan"))
                .andExpect(jsonPath("$.softwareSkills[1]").value("Renga"))
                .andExpect(jsonPath("$.education[0]").value("академия бекенда"))
                .andExpect(jsonPath("$.education[1]").value("вшэ"))
                .andExpect(jsonPath("$.socialNetworks[0]").value("вк ком"))
                .andExpect(jsonPath("$.socialNetworks[1]").value("одноклассники ком"))
                .andExpect(jsonPath("$.about").value("Я кирюха"));
    }

    @Test
    public void uploadProfileSetting() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initProfileInfo();

        ProfileSettingInfoDTO profileSettingInfoDTO = new ProfileSettingInfoDTO(
                "new photo",
                "не шульжик",
                "не кирилл",
                "вообще мужской но не мужской для теста ладно",
                "Пермь",
                "2022-09-17",
                "test@mail.ru",
                "Безработный",
                true,
                true
        );

        mockMvc.perform(post("/api/profile/upload-profile-setting")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileSettingInfoDTO)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/profile/profile-setting-info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surname").value("не шульжик"))
                .andExpect(jsonPath("$.name").value("не кирилл"))
                .andExpect(jsonPath("$.gender").value("вообще мужской но не мужской для теста ладно"))
                .andExpect(jsonPath("$.city").value("Пермь"))
                .andExpect(jsonPath("$.birthday").value("2022-09-17"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.status").value("Безработный"))
                .andExpect(jsonPath("$.isHideBirthday").value(true))
                .andExpect(jsonPath("$.isPublic").value(true));
    }
}
