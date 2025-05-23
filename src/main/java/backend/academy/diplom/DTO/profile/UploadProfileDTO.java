package backend.academy.diplom.DTO.profile;

import java.util.List;

public record UploadProfileDTO(
        List<String> sectionAndStamps,
        List<String> softwareSkills,
        List<String> education,
        List<String> contacts,
        String about
) {
}
