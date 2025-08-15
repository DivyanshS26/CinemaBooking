package kfru.architecture.cinemabooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kfru.architecture.cinemabooking.dto.MovieDTO;
import kfru.architecture.cinemabooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movies", description = "Movie management operations")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieve a list of all available movies")
    @ApiResponse(responseCode = "200", description = "Movies retrieved successfully")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieve a specific movie by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie found"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        MovieDTO movieDTO = movieService.getMovieById(id);
        return ResponseEntity.ok(movieDTO);
    }

    @PostMapping
    @Operation(summary = "Create new movie", description = "Add a new movie to the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movie created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid movie data")
    })
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO savedMovie = movieService.createMovie(movieDTO);
        URI location = URI.create("/movies/" + savedMovie.getId());
        return ResponseEntity.created(location).body(savedMovie);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update movie", description = "Update an existing movie's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<MovieDTO> update(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO updatedMovie = movieService.updateMovie(id, movieDTO);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie", description = "Remove a movie from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }


}
