package kfru.architecture.cinemabooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfru.architecture.cinemabooking.dto.ShowtimeDTO;
import kfru.architecture.cinemabooking.entity.Movie;
import kfru.architecture.cinemabooking.entity.Showtime;
import kfru.architecture.cinemabooking.exception.ResourceNotFoundException;
import kfru.architecture.cinemabooking.repository.MovieRepository;
import kfru.architecture.cinemabooking.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository repo;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Movie movie;
    private Showtime showtime1, showtime2;
    private ShowtimeDTO showtimeDTO1, showtimeDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = createMovie(1L, "Inception", 148, "Sci-Fi");

        showtime1 = createShowtime(1L, LocalDateTime.now().plusDays(1), 50, movie);
        showtime2 = createShowtime(2L, LocalDateTime.now().plusDays(2), 30, movie);

        showtimeDTO1 = createShowtimeDTO(1L, LocalDateTime.now().plusDays(1), 50, 1L);
        showtimeDTO2 = createShowtimeDTO(2L, LocalDateTime.now().plusDays(2), 30, 1L);
    }

    @Test
    void getAllShowtimes_ShouldReturnAllShowtimesAsDTO() {
        when(repo.findAll()).thenReturn(Arrays.asList(showtime1, showtime2));

        List<ShowtimeDTO> result = showtimeService.getAllShowtimes();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAvailableSeats()).isEqualTo(50);
        assertThat(result.get(1).getAvailableSeats()).isEqualTo(30);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getShowtimeById_WhenShowtimeExists_ShouldReturnShowtimeDTO() {
        when(repo.findById(1L)).thenReturn(Optional.of(showtime1));

        ShowtimeDTO result = showtimeService.getShowtimeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMovieId()).isEqualTo(1L);
    }

    @Test
    void getShowtimeById_WhenShowtimeNotExists_ShouldThrowResourceNotFoundException() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> showtimeService.getShowtimeById(999L)
        );
        assertThat(exception.getMessage()).contains("Showtime not found with id 999");
    }

    @Test
    void createShowtime_ShouldSaveAndReturnShowtimeDTO() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(objectMapper.convertValue(any(ShowtimeDTO.class), eq(Showtime.class))).thenReturn(showtime1);
        when(repo.save(any(Showtime.class))).thenReturn(showtime1);

        ShowtimeDTO result = showtimeService.createShowtime(showtimeDTO1);

        assertThat(result).isNotNull();
        assertThat(result.getMovieId()).isEqualTo(1L);
        verify(repo, times(1)).save(any(Showtime.class));
    }

    @Test
    void createShowtime_WhenMovieNotExists_ShouldThrowResourceNotFoundException() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        showtimeDTO1.setMovieId(999L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> showtimeService.createShowtime(showtimeDTO1)
        );
        assertThat(exception.getMessage()).contains("Movie not found with id 999");
    }

    @Test
    void deleteShowtime_ShouldCallRepositoryDelete() {
        showtimeService.deleteShowtime(1L);
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

    private Showtime createShowtime(Long id, LocalDateTime dateTime, int seats, Movie movie) {
        Showtime showtime = new Showtime();
        showtime.setId(id);
        showtime.setShowDateTime(dateTime);
        showtime.setAvailableSeats(seats);
        showtime.setMovie(movie);
        return showtime;
    }

    private ShowtimeDTO createShowtimeDTO(Long id, LocalDateTime dateTime, int seats, Long movieId) {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(id);
        dto.setShowDateTime(dateTime);
        dto.setAvailableSeats(seats);
        dto.setMovieId(movieId);
        return dto;
    }
}
