package kfru.architecture.cinemabooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfru.architecture.cinemabooking.dto.BookingDTO;
import kfru.architecture.cinemabooking.entity.Booking;
import kfru.architecture.cinemabooking.entity.Showtime;
import kfru.architecture.cinemabooking.exception.ResourceNotFoundException;
import kfru.architecture.cinemabooking.repository.BookingRepository;
import kfru.architecture.cinemabooking.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private ShowtimeRepository showtimeRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepo.findAll();
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
        return convertToDTO(booking);
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = objectMapper.convertValue(bookingDTO, Booking.class);
        Showtime showtime = showtimeRepo.findById(bookingDTO.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id " + bookingDTO.getShowtimeId()));
        booking.setShowtime(showtime);

        Booking saved = bookingRepo.save(booking);
        return convertToDTO(saved);
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
        Booking updatedBooking = objectMapper.convertValue(bookingDTO, Booking.class);
        updatedBooking.setId(existingBooking.getId());
        Showtime showtime = showtimeRepo.findById(bookingDTO.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id " + bookingDTO.getShowtimeId()));
        updatedBooking.setShowtime(showtime);

        Booking saved = bookingRepo.save(updatedBooking);
        return convertToDTO(saved);
    }

    public void deleteBooking(Long id) {
        bookingRepo.deleteById(id);
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCustomerName(booking.getCustomerName());
        dto.setSeatsBooked(booking.getSeatsBooked());
        dto.setShowtimeId(booking.getShowtime().getId());
        return dto;
    }

}
