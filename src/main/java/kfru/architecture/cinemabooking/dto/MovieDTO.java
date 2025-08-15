package kfru.architecture.cinemabooking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import kfru.architecture.cinemabooking.entity.Showtime;
import lombok.*;

import java.util.List;

@Data
public class MovieDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @Min(value = 1, message = "Duration should be at least 1 minute")
    private int duration;

    @NotBlank(message = "Genre is required")
    private String genre;

    private List<Showtime> showtimeList;
}
