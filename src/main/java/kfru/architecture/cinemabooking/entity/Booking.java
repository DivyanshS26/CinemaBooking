package kfru.architecture.cinemabooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Min(value = 1, message = "At least 1 seat must be booked")
    private int seatsBooked;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    @NotNull(message = "Showtime is required")
    private Showtime showtime;
}
