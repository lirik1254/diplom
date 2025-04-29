package backend.academy.diplom.services;

import backend.academy.diplom.DTO.profile.ProfileData;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.profile.*;
import backend.academy.diplom.utils.CreateAccessToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserSectionAndStampRepository userSectionAndStampRepository;
    private final UserSocialNetworkRepository userSocialNetworkRepository;
    private final UserSoftwareSkillRepository userSoftwareSkillRepository;
    private final SocialNetworkRepository socialNetworkRepository;
    private final SectionAndStampRepository sectionAndStampRepository;
    private final SoftwareSkillRepository softwareSkillRepository;
    private final S3Service s3Service;
    private final CreateAccessToken createAccessToken;


    private static final String FIRST_NAME_KEY = "firstName";
    private static final String LAST_NAME_KEY = "lastName";
    private static final String CITY = "city";
    private static final String GENDER = "gender";
    private static final String SECTION = "section";
    private static final String SKILL = "skill";
    private static final String SOCIAL = "social";
    private static final String BIRTH_DATE = "birth_date";
    private static final String DELETED_RESUME = "deletedResume";
    private static final String DELETED_DIPLOMA = "deletedDiploma";
    private static final String DELETED_PHOTO = "deletedPhoto";
    private static final String RESUME_PATH = "resume";
    private static final String DIPLOMA_PATH = "diploma";
    private static final String PHOTO_PATH = "photo";

    private String lastName;
    private String firstName;
    private String city;
    private String gender;
    private String section;
    private String skill;
    private String social;
    private LocalDate birthDate;
    private String token;
    private String email;
    private User user;
    private Long userId;
    private Boolean deletedResume;
    private Boolean deletedDiploma;
    private Boolean deletedPhoto;

    private String resumePath = null;
    private String diplomaPath = null;
    private String photoPath = null;

    @Transactional
    public void upload(String authHeader, Map<String, String> fields) throws IOException {

        init(fields, authHeader);

        // Сначала обновление дефолтных полей у user
        userRepository.updateStringFields(email, lastName, firstName, city, gender, birthDate);

        // После добавление в коллекции, связанные с user
        processCollections();

        deleteFiles(authHeader);
    }


    private String setField(Map<String, String> fields, String key) {
        if (fields.containsKey(key)) {
            return fields.get(key);
        }
        return null;
    }

    private void init(Map<String, String> fields, String authHeader) {
        lastName = setField(fields, LAST_NAME_KEY);
        firstName = setField(fields, FIRST_NAME_KEY);
        city = setField(fields, CITY);
        gender = setField(fields, GENDER);
        section = setField(fields, SECTION);
        skill = setField(fields, SKILL);
        social = setField(fields, SOCIAL);
        resumePath = setField(fields, RESUME_PATH);
        photoPath = setField(fields, photoPath);
        diplomaPath = setField(fields, diplomaPath);

        if (fields.containsKey(BIRTH_DATE)) {
            String birth = fields.get(BIRTH_DATE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            birthDate = LocalDate.parse(birth, formatter);
        }

        token = authHeader.substring(7);
        email = createAccessToken.getEmailFromJwtToken(token);

        user = userRepository.findByEmail(email).getFirst();
        userId = user.getId();

        deletedResume = Boolean.valueOf(fields.get(DELETED_RESUME));
        deletedDiploma = Boolean.valueOf(fields.get(DELETED_DIPLOMA));
        deletedPhoto = Boolean.valueOf(fields.get(DELETED_PHOTO));
    }

    private void processCollections() {
        if (section != null) {
            Set<String> sections = new HashSet<>(List.of(section.split(" ")));
            userSectionAndStampRepository.deleteByUserId(userId);
            sections.forEach(stringSection -> userSectionAndStampRepository.addUserSection(userId,
                    section));
        }
        if (skill != null) {
            Set<String> skills = new HashSet<>(List.of(skill.split(" ")));
            userSoftwareSkillRepository.deleteByUserId(userId);
            skills.forEach(skillString -> userSoftwareSkillRepository.addSoftwareSkill(userId,
                    skillString));
        }

        if (social != null) {
            Set<String> socials = new HashSet<>(List.of(social.split(" ")));
            userSocialNetworkRepository.deleteByUserId(userId);
            socials.forEach(socialString -> userSocialNetworkRepository.addSocialNetwork(userId,
                    socialString));
        }
    }

    private void deleteFiles(String authHeader) {
        if (deletedResume) {
            s3Service.deleteFile(authHeader, "resume");
        }

        if (deletedDiploma) {
            s3Service.deleteFile(authHeader, "diploma");
        }

        if (deletedPhoto) {
            s3Service.deleteFile(authHeader, "photo");
        }
    }

    public ProfileData getProfile(String authHeader) throws IOException {
        token = authHeader.substring(7);
        email = createAccessToken.getEmailFromJwtToken(token);

        User user = userRepository.findByEmail(email).getFirst();
        Long userId = user.getId();

        Map<String, String> returnData = new HashMap<>();
        returnData.put(FIRST_NAME_KEY, user.getName());
        returnData.put(LAST_NAME_KEY, user.getSurname());
        returnData.put(CITY, user.getCity());
        returnData.put(GENDER, user.getGender());
        returnData.put(BIRTH_DATE,
                user.getBirthDate() == null ? null : user.getBirthDate().toString());

        String socialNetworks = String.join(" ",
                socialNetworkRepository.getSocialNetworksByUserId(userId));
        String sectionAndStamps = String.join(" ",
                sectionAndStampRepository.getSectionAndStampByUserid(userId));
        String softwareSkills = String.join(" ",
                softwareSkillRepository.getSoftwareSkillByUserId(userId));

        returnData.put(SECTION, sectionAndStamps);
        returnData.put(SKILL, softwareSkills);
        returnData.put(SOCIAL, socialNetworks);

        return new ProfileData(returnData);
    }

}
