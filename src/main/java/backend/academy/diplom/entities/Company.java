package backend.academy.diplom.entities;

import lombok.Data;

@Data
public class Company {
    private Long id;
    private String name;
    private String status;
    private String city;
    private String photoPath;
}
