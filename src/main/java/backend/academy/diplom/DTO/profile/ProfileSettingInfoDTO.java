package backend.academy.diplom.DTO.profile;

public record ProfileSettingInfoDTO(
        String photoPath,
        String surname,
        String name,
        String gender,
        String city,
        String birthday,
        String email,
        String status,
        Boolean isHideBirthday,
        Boolean isPublic
) {
}
