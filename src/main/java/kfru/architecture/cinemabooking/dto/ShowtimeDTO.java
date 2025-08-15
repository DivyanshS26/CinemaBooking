package kfru.architecture.cinemabooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class ShowtimeDTO {
    @Schema(description = "Showtime unique identifier", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Show date and time is required")
    @Future(message = "Showtime must be in the future")
    @Schema(description = "Date and time of the show", example = "2025-08-20T19:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime showDateTime;

    @Min(value = 1, message = "Available seats must be at least 1")
    @Schema(description = "Number of available seats", example = "50",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "500")
    private int availableSeats;

    @NotNull(message = "Movie ID is required")
    @Schema(description = "Movie ID for this showtime", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long movieId;
}
