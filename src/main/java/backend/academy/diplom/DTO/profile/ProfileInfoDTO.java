package backend.academy.diplom.DTO.profile;

import java.util.List;

public record ProfileInfoDTO(
        List<String> sectionAndStamps,
        List<String> softwareSkills,
        String resumePath,
        String resumeName,
        List<String> education,
        String diplomaPath,
        String diplomaName,
        List<String> socialNetworks,
        String telegram,
        String about
) {
}
