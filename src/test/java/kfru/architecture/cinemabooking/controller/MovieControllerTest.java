package kfru.architecture.cinemabooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfru.architecture.cinemabooking.dto.MovieDTO;
import kfru.architecture.cinemabooking.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMovies_ShouldReturnListOfMovies() throws Exception {
        MovieDTO movie1 = createMovieDTO(1L, "Inception", 148, "Sci-Fi");
        MovieDTO movie2 = createMovieDTO(2L, "Avatar", 162, "Action");
        when(movieService.getAllMovies()).thenReturn(Arrays.asList(movie1, movie2));

        mockMvc.perform(get("/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))                    // ✅ Array length
                .andExpect(jsonPath("$[0].title").value("Inception"))          // ✅ First item
                .andExpect(jsonPath("$[1].title").value("Avatar"));            // ✅ Second item
    }



    @Test
    void getMovieById_ShouldReturnMovie() throws Exception {
        MovieDTO movie = createMovieDTO(1L, "Inception", 148, "Sci-Fi");
        when(movieService.getMovieById(1L)).thenReturn(movie);

        mockMvc.perform(get("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void createMovie_ShouldReturnCreatedMovie() throws Exception {
        MovieDTO inputMovie = createMovieDTO(null, "New Movie", 120, "Drama");
        MovieDTO savedMovie = createMovieDTO(1L, "New Movie", 120, "Drama");
        when(movieService.createMovie(any(MovieDTO.class))).thenReturn(savedMovie);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputMovie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Movie"));
    }

    @Test
    void updateMovie_ShouldReturnUpdatedMovie() throws Exception {
        MovieDTO updatedMovie = createMovieDTO(1L, "Updated Movie", 130, "Comedy");
        when(movieService.updateMovie(eq(1L), any(MovieDTO.class))).thenReturn(updatedMovie);

        mockMvc.perform(put("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Movie"));
    }

    @Test
    void deleteMovie_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/movies/1"))
                .andExpect(status().isNoContent());
    }

    private MovieDTO createMovieDTO(Long id, String title, int duration, String genre) {
        MovieDTO dto = new MovieDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDuration(duration);
        dto.setGenre(genre);
        return dto;
    }
}
