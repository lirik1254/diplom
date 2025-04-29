package backend.academy.diplom.DTO.search;

import java.util.List;

public record ProjectsDTO(Long id, String name, Long authorId, String fullName, Integer likeCount,
                          String photoUrl, List<String> sectionAndStamps, List<String> softwareSkills) {
}
