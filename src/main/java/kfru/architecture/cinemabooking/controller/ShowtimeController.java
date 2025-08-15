package kfru.architecture.cinemabooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kfru.architecture.cinemabooking.dto.ShowtimeDTO;
import kfru.architecture.cinemabooking.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/showtimes")
@Tag(name = "Showtimes", description = "Showtime management operations")
public class ShowtimeController {

    @Autowired
    private ShowtimeService service;

    @GetMapping
    @Operation(summary = "Get all Showtimes", description = "Retrieve a list of all available showtimes")
    @ApiResponse(responseCode = "200", description = "Showtimes retrieved successfully")
    public ResponseEntity<List<ShowtimeDTO>> getAllShowtimes() {
        List<ShowtimeDTO> showtimes = service.getAllShowtimes();
        return ResponseEntity.ok(showtimes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get showtime by ID", description = "Retrieve a specific showtime by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Showtime found"),
            @ApiResponse(responseCode = "404", description = "Showtime not found")
    })
    public ResponseEntity<ShowtimeDTO> getShowtimeById(@PathVariable Long id) {
        ShowtimeDTO showtime = service.getShowtimeById(id);
        return ResponseEntity.ok(showtime);
    }

    @PostMapping
    @Operation(summary = "Create new showtime", description = "Add a new showtime to the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Showtime created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid showtime data")
    })
    public ResponseEntity<ShowtimeDTO> createShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO savedShowtime = service.createShowtime(showtimeDTO);
        URI location = URI.create("/showtimes/" + savedShowtime.getId());
        return ResponseEntity.created(location).body(savedShowtime);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update showtime", description = "Update an existing showtime's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Showtime updated successfully"),
            @ApiResponse(responseCode = "404", description = "Showtime not found")
    })
    public ResponseEntity<ShowtimeDTO> update(@PathVariable Long id, @Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO updatedShowtime = service.updateShowtime(id, showtimeDTO);
        return ResponseEntity.ok(updatedShowtime);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete showtime", description = "Remove a showtime from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Showtime deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Showtime not found")
    })
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        service.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}
