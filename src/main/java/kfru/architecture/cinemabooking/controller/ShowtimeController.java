package kfru.architecture.cinemabooking.controller;

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
public class ShowtimeController {

    @Autowired
    private ShowtimeService service;

    @GetMapping
    public ResponseEntity<List<ShowtimeDTO>> getAllShowtimes() {
        List<ShowtimeDTO> showtimes = service.getAllShowtimes();
        return ResponseEntity.ok(showtimes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeDTO> getShowtimeById(@PathVariable Long id) {
        ShowtimeDTO showtime = service.getShowtimeById(id);
        return ResponseEntity.ok(showtime);
    }

    @PostMapping
    public ResponseEntity<ShowtimeDTO> createShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO savedShowtime = service.createShowtime(showtimeDTO);
        URI location = URI.create("/showtimes/" + savedShowtime.getId());
        return ResponseEntity.created(location).body(savedShowtime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeDTO> update(@PathVariable Long id, @Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO updatedShowtime = service.updateShowtime(id, showtimeDTO);
        return ResponseEntity.ok(updatedShowtime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        service.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}
