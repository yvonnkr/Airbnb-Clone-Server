package com.yvolabs.airbnbclone.booking.repository;

import com.yvolabs.airbnbclone.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 18/09/2024
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
