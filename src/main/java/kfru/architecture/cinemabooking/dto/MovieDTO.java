package kfru.architecture.cinemabooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import kfru.architecture.cinemabooking.entity.Showtime;
import lombok.*;

import java.util.List;

@Data
@Schema(description = "Movie data transfer object")
public class MovieDTO {
    @Schema(description = "Movie unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Schema(description = "Movie title", example = "Inception",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Min(value = 1, message = "Duration should be at least 1 minute")
    @Schema(description = "Movie duration in minutes", example = "148",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1", maximum = "500")
    private int duration;

    @NotBlank(message = "Genre is required")
    @Schema(description = "Movie genre", example = "Sci-Fi",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String genre;

}
