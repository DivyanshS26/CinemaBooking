package kfru.architecture.cinemabooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfru.architecture.cinemabooking.dto.BookingDTO;
import kfru.architecture.cinemabooking.entity.Booking;
import kfru.architecture.cinemabooking.entity.Movie;
import kfru.architecture.cinemabooking.entity.Showtime;
import kfru.architecture.cinemabooking.exception.ResourceNotFoundException;
import kfru.architecture.cinemabooking.repository.BookingRepository;
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

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private ShowtimeRepository showtimeRepo;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BookingService bookingService;

    private Showtime showtime;
    private Booking booking1, booking2;
    private BookingDTO bookingDTO1, bookingDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Movie movie = createMovie(1L, "Inception", 148, "Sci-Fi");
        showtime = createShowtime(1L, LocalDateTime.now().plusDays(1), 50, movie);

        booking1 = createBooking(1L, "John Doe", 2, showtime);
        booking2 = createBooking(2L, "Jane Smith", 1, showtime);

        bookingDTO1 = createBookingDTO(1L, "John Doe", 2, 1L);
        bookingDTO2 = createBookingDTO(2L, "Jane Smith", 1, 1L);
    }

    @Test
    void getAllBookings_ShouldReturnAllBookingsAsDTO() {
        when(bookingRepo.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        List<BookingDTO> result = bookingService.getAllBookings();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCustomerName()).isEqualTo("John Doe");
        assertThat(result.get(1).getSeatsBooked()).isEqualTo(1);
        verify(bookingRepo, times(1)).findAll();
    }

    @Test
    void getBookingById_WhenBookingExists_ShouldReturnBookingDTO() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking1));

        BookingDTO result = bookingService.getBookingById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void getBookingById_WhenBookingNotExists_ShouldThrowResourceNotFoundException() {
        when(bookingRepo.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getBookingById(999L)
        );
        assertThat(exception.getMessage()).contains("Booking not found with id 999");
    }

    @Test
    void createBooking_ShouldSaveAndReturnBookingDTO() {
        Booking mockBooking = createBooking(null, "John Doe", 2, showtime);

        when(objectMapper.convertValue(any(BookingDTO.class), eq(Booking.class)))
                .thenReturn(mockBooking);
        when(objectMapper.convertValue(any(Booking.class), eq(BookingDTO.class)))
                .thenReturn(bookingDTO1);

        when(showtimeRepo.findById(1L)).thenReturn(Optional.of(showtime));
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking1);

        BookingDTO result = bookingService.createBooking(bookingDTO1);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        verify(bookingRepo, times(1)).save(any(Booking.class));
    }


    @Test
    void createBooking_WhenShowtimeNotExists_ShouldThrowResourceNotFoundException() {
        when(showtimeRepo.findById(999L)).thenReturn(Optional.empty());
        bookingDTO1.setShowtimeId(999L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(bookingDTO1)
        );
        assertThat(exception.getMessage()).contains("Showtime not found with id 999");
    }

    @Test
    void deleteBooking_ShouldCallRepositoryDelete() {
        bookingService.deleteBooking(1L);
        verify(bookingRepo, times(1)).deleteById(1L);
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

    private Booking createBooking(Long id, String customerName, int seats, Showtime showtime) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setCustomerName(customerName);
        booking.setSeatsBooked(seats);
        booking.setShowtime(showtime);
        return booking;
    }

    private BookingDTO createBookingDTO(Long id, String customerName, int seats, Long showtimeId) {
        BookingDTO dto = new BookingDTO();
        dto.setId(id);
        dto.setCustomerName(customerName);
        dto.setSeatsBooked(seats);
        dto.setShowtimeId(showtimeId);
        return dto;
    }
}
