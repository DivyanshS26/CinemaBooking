package kfru.architecture.cinemabooking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class ShowtimeDTO {

    private Long id;

    @NotNull(message = "Show date and time is required")
    @Future(message = "Showtime must be in the future")
    private LocalDateTime showDateTime;

    @Min(value = 1, message = "Available seats must be at least 1")
    private int availableSeats;

    @NotNull(message = "Movie ID is required")
    private Long movieId;
}
