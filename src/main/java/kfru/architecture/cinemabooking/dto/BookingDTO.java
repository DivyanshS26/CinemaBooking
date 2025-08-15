package kfru.architecture.cinemabooking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class BookingDTO {
    private Long id;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Min(value = 1, message = "At least 1 seat must be booked")
    private int seatsBooked;

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
}
