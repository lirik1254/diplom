package backend.academy.diplom.entities.user;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String password;
    private Date birthDate;
    private String city;
    private String status;
    private String resumePath;
    private String diplomaPath;
    private String photoPath;
    private String gender;
    private String about;
    private String telegram;
    private Boolean hideBirthday;
    private Boolean isPublic;
}
