package kfru.architecture.cinemabooking.repository;

import kfru.architecture.cinemabooking.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
}
