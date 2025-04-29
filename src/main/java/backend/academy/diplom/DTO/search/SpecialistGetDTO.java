package backend.academy.diplom.DTO.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class SpecialistGetDTO {
    private Long id;
    private String status;
    private String photoUrl;
    private String fullName;
    private String city;
    private String education;
    private List<String> sectionAndStamp;
    private List<String> softwareSkill;
}
