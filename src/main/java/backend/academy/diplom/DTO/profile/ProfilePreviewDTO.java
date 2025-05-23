package backend.academy.diplom.DTO.profile;

public record ProfilePreviewDTO(
        String fullName,
        Integer age,
        String city,
        String status,
        String photoPath
) {}
