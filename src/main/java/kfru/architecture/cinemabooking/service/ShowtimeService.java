package kfru.architecture.cinemabooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfru.architecture.cinemabooking.dto.ShowtimeDTO;
import kfru.architecture.cinemabooking.entity.Movie;
import kfru.architecture.cinemabooking.entity.Showtime;
import kfru.architecture.cinemabooking.exception.ResourceNotFoundException;
import kfru.architecture.cinemabooking.repository.MovieRepository;
import kfru.architecture.cinemabooking.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository repo;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<ShowtimeDTO> getAllShowtimes() {
        List<Showtime> showtimes = repo.findAll();
        return showtimes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShowtimeDTO getShowtimeById(Long id) {
        Showtime showtime = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id " + id));
        return convertToDTO(showtime);
    }

    public ShowtimeDTO createShowtime(ShowtimeDTO showtimeDTO) {
        Showtime showtime = objectMapper.convertValue(showtimeDTO, Showtime.class);

        Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + showtimeDTO.getMovieId()));
        showtime.setMovie(movie);

        Showtime saved = repo.save(showtime);
        return convertToDTO(saved);
    }

    public ShowtimeDTO updateShowtime(Long id, ShowtimeDTO updatedShowtimeDTO) {
        Showtime existingShowtime = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id " + id));

        Showtime updatedShowtime = objectMapper.convertValue(updatedShowtimeDTO, Showtime.class);
        updatedShowtime.setId(existingShowtime.getId());

        Movie movie = movieRepository.findById(updatedShowtimeDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + updatedShowtimeDTO.getMovieId()));
        updatedShowtime.setMovie(movie);

        Showtime savedShowtime = repo.save(updatedShowtime);
        return convertToDTO(savedShowtime);
    }

    public void deleteShowtime(Long id) {
        repo.deleteById(id);
    }

    private ShowtimeDTO convertToDTO(Showtime showtime) {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(showtime.getId());
        dto.setShowDateTime(showtime.getShowDateTime());
        dto.setAvailableSeats(showtime.getAvailableSeats());
        dto.setMovieId(showtime.getMovie().getId());
        return dto;
    }

}
