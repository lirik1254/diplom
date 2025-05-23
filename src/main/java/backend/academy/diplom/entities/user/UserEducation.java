package backend.academy.diplom.entities.user;

import lombok.Data;

@Data
public class UserEducation {
    private Long id;
    private Long userId;
    private String education;
}
