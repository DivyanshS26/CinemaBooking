package kfru.architecture.cinemabooking.repository;

import kfru.architecture.cinemabooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
