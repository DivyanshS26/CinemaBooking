package kfru.architecture.cinemabooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfru.architecture.cinemabooking.dto.MovieDTO;
import kfru.architecture.cinemabooking.entity.Movie;
import kfru.architecture.cinemabooking.exception.ResourceNotFoundException;
import kfru.architecture.cinemabooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = repo.findAll();
        return movies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO getMovieById(Long id) {
        Movie movie = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
        return convertToDTO(movie);
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = repo.save(movie);
        return convertToDTO(savedMovie);
    }

    public MovieDTO updateMovie(Long id, MovieDTO updatedMovieDTO) {
        Movie existingMovie = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));

        Movie updatedMovie = convertToEntity(updatedMovieDTO);
        updatedMovie.setId(existingMovie.getId());

        Movie savedMovie = repo.save(updatedMovie);
        return convertToDTO(savedMovie);
    }

    public void deleteMovie(Long id) {
        repo.deleteById(id);
    }

    private MovieDTO convertToDTO(Movie movie) {
        return objectMapper.convertValue(movie, MovieDTO.class);
    }

    private Movie convertToEntity(MovieDTO dto) {
        return objectMapper.convertValue(dto, Movie.class);
    }
}
