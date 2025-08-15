package kfru.architecture.cinemabooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Schema(description = "Booking data transfer object")
public class BookingDTO {
    @Schema(description = "Booking unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Schema(description = "Customer Name", example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerName;

    @Min(value = 1, message = "At least 1 seat must be booked")
    @Schema(description = "Seats booked", example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1", maximum = "500")
    private int seatsBooked;

    @NotNull(message = "Showtime ID is required")
    @Schema(description = "Showtime ID for this booking", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long showtimeId;
}
