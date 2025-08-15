package kfru.architecture.cinemabooking.repository;

import kfru.architecture.cinemabooking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
