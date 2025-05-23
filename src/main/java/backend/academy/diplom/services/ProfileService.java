package backend.academy.diplom.services;

import backend.academy.diplom.DTO.profile.*;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.profile.*;
import backend.academy.diplom.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserSectionAndStampRepository userSectionAndStampRepository;
    private final UserSocialNetworkRepository userSocialNetworkRepository;
    private final UserEducationRepository userEducationRepository;
    private final UserSoftwareSkillRepository userSoftwareSkillRepository;
    private final SectionAndStampRepository sectionAndStampRepository;
    private final SoftwareSkillRepository softwareSkillRepository;

    private final JwtUtils jwtUtils;
    private final FileService fileService;

    public ProfilePreviewDTO getProfilePreview(String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        String fullName = user.getName() + " " + user.getSurname();
        Date birthDate = user.getBirthDate();
        Integer age = null;
        if (birthDate != null) {
            age = getAge(user.getBirthDate());
        }
        String photoPath = user.getPhotoPath();
        if (photoPath != null) {
            photoPath = fileService.getPresignedLink(user.getPhotoPath());
        }

        return new ProfilePreviewDTO(fullName, age, user.getCity(), user.getStatus(), photoPath);
    }

    private Integer getAge(Date birthDate) {
        LocalDate birthLocal = Instant.ofEpochMilli(birthDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate today = LocalDate.now();

        return Period.between(birthLocal, today).getYears();
    }

    public ProfileInfoDTO getProfileInfo(String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        Long userId = user.getId();

        List<String> sectionAndStamp = sectionAndStampRepository.getSectionAndStampByUserid(userId);
        List<String> softwareSkills = softwareSkillRepository.getSoftwareSkillByUserId(userId);

        String diplomaPath = user.getDiplomaPath();
        String diplomaName = diplomaPath;
        if (diplomaPath != null && !Objects.equals(diplomaPath, "")) {
            diplomaPath = fileService.getPresignedLink(diplomaPath);
        }

        String resumePath = user.getResumePath();
        String resumeName = resumePath;
        if (resumePath != null && !Objects.equals(resumePath, "")) {
            resumePath = fileService.getPresignedLink(resumePath);
        }

        List<String> userEducation = userEducationRepository.getEducationByUserId(userId);
        List<String> userSocialNetworks = userSocialNetworkRepository.getSocialNetworkByUserId(userId);

        return new ProfileInfoDTO(sectionAndStamp, softwareSkills, resumePath, resumeName,
                userEducation, diplomaPath, diplomaName, userSocialNetworks,
                user.getTelegram(), user.getAbout());
    }

    @Transactional
    public void updateProfile(String authHeader, UploadProfileDTO uploadProfileDTO) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        long userId = user.getId();

        userRepository.updateAbout(userId, uploadProfileDTO.about());

        userEducationRepository.deleteEducation(userId);
        uploadProfileDTO.education().forEach(education ->
                userEducationRepository.addEducation(userId, education));

        userSocialNetworkRepository.deleteByUserId(userId);
        uploadProfileDTO.contacts().forEach(contact ->
                userSocialNetworkRepository.addSocialNetwork(userId, contact));

        userSectionAndStampRepository.deleteByUserId(userId);
        uploadProfileDTO.sectionAndStamps().forEach(section ->
                userSectionAndStampRepository.addUserSection(userId, section));

        userSoftwareSkillRepository.deleteByUserId(userId);
        uploadProfileDTO.softwareSkills().forEach(software ->
                userSoftwareSkillRepository.addSoftwareSkill(userId, software));
    }

    public ProfileSettingInfoDTO getProfileSettingInfo(String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        String photoPath = user.getPhotoPath();
        if (photoPath != null && !Objects.equals("", photoPath)) {
            photoPath = fileService.getPresignedLink(photoPath);
        }
        return new ProfileSettingInfoDTO(photoPath, user.getSurname(), user.getName(), user.getGender(),
                user.getCity(), user.getBirthDate().toString(), user.getEmail(), user.getStatus(), user.getHideBirthday(),
                user.getIsPublic());
    }

    public void uploadProfileSetting(ProfileSettingInfoDTO profileSettingInfoDTO, String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date updateDate = null;
        try {
            updateDate = sdf.parse(profileSettingInfoDTO.birthday());
        } catch (Exception ignored) {}
        userRepository.updateProfileSettingFields(
            user.getId(), profileSettingInfoDTO.name(), profileSettingInfoDTO.surname(),
                profileSettingInfoDTO.gender(), profileSettingInfoDTO.city(),
                updateDate, profileSettingInfoDTO.email(),
                profileSettingInfoDTO.status(), profileSettingInfoDTO.isHideBirthday(),
                profileSettingInfoDTO.isPublic()
        );
    }
}
