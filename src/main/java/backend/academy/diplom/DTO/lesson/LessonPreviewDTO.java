package backend.academy.diplom.DTO.lesson;

public record LessonPreviewDTO(
        Long id,
        String name,
        String authorName,
        String authorPhotoPath) {
}
