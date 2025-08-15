package kfru.architecture.cinemabooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfru.architecture.cinemabooking.dto.MovieDTO;
import kfru.architecture.cinemabooking.entity.Movie;
import kfru.architecture.cinemabooking.exception.ResourceNotFoundException;
import kfru.architecture.cinemabooking.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
class MovieServiceTest {

    @Mock
    private MovieRepository repo;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MovieService movieService;

    private Movie movie1, movie2;
    private MovieDTO movieDTO1, movieDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setDuration(148);
        movie1.setGenre("Sci-Fi");

        movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("It Ends with Us");
        movie2.setDuration(130);
        movie2.setGenre("Romance/Drama");

        movieDTO1 = new MovieDTO();
        movieDTO1.setId(1L);
        movieDTO1.setTitle("Inception");
        movieDTO1.setDuration(148);
        movieDTO1.setGenre("Sci-Fi");

        movieDTO2 = new MovieDTO();
        movieDTO2.setId(2L);
        movieDTO2.setTitle("It Ends with Us");
        movieDTO2.setDuration(130);
        movieDTO2.setGenre("Romance/Drama");
    }
    @Test
    void getAllMovies_ShouldReturnAllMoviesAsDTO() {
        when(repo.findAll()).thenReturn(Arrays.asList(movie1, movie2));
        when(objectMapper.convertValue(movie1, MovieDTO.class)).thenReturn(movieDTO1);
        when(objectMapper.convertValue(movie2, MovieDTO.class)).thenReturn(movieDTO2);

        List<MovieDTO> result = movieService.getAllMovies();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Inception");
        assertThat(result.get(1).getGenre()).isEqualTo("Romance/Drama");
    }


    @Test
    void getMovieById_WhenMovieExists_ShouldReturnMovieDTO() {
        when(repo.findById(1L)).thenReturn(Optional.of(movie1));
        when(objectMapper.convertValue(movie1, MovieDTO.class)).thenReturn(movieDTO1);

        MovieDTO result = movieService.getMovieById(1L);
        log.info("result: {}", result);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Inception");
    }





    @Test
    void getMovieById_WhenMovieNotExists_ShouldThrowResourceNotFoundException() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> movieService.getMovieById(999L)
        );
        assertThat(exception.getMessage()).contains("Movie not found with id 999");
    }

    @Test
    void updateMovie_WhenMovieExists_ShouldUpdateAndReturnMovieDTO() {
        Movie mockUpdatedMovie = createMovie(1L, "Updated Inception", 150, "Updated Sci-Fi");

        when(objectMapper.convertValue(any(MovieDTO.class), eq(Movie.class)))
                .thenReturn(mockUpdatedMovie);
        when(objectMapper.convertValue(any(Movie.class), eq(MovieDTO.class)))
                .thenReturn(movieDTO1);

        when(repo.findById(1L)).thenReturn(Optional.of(movie1));
        when(repo.save(any(Movie.class))).thenReturn(movie1);

        MovieDTO result = movieService.updateMovie(1L, movieDTO1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(repo, times(1)).save(any(Movie.class));
    }

    @Test
    void createMovie_ShouldSaveAndReturnMovieDTO() {
        Movie mockMovie = createMovie(null, "Inception", 148, "Sci-Fi");

        when(objectMapper.convertValue(any(MovieDTO.class), eq(Movie.class)))
                .thenReturn(mockMovie);
        when(objectMapper.convertValue(any(Movie.class), eq(MovieDTO.class)))
                .thenReturn(movieDTO1);

        when(repo.save(any(Movie.class))).thenReturn(movie1);

        MovieDTO result = movieService.createMovie(movieDTO1);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Inception");
        verify(repo, times(1)).save(any(Movie.class));
    }


    @Test
    void updateMovie_WhenMovieNotExists_ShouldThrowResourceNotFoundException() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> movieService.updateMovie(999L, movieDTO1));
    }

    @Test
    void deleteMovie_ShouldCallRepositoryDelete() {
        movieService.deleteMovie(1L);
        verify(repo, times(1)).deleteById(1L);
    }




    private Movie createMovie(Long id, String title, int duration, String genre) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setDuration(duration);
        movie.setGenre(genre);
        return movie;
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
