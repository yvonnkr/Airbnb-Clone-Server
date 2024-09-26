package com.yvolabs.airbnbclone.booking.mapper;

import com.yvolabs.airbnbclone.booking.application.dto.BookedDateDTO;
import com.yvolabs.airbnbclone.booking.application.dto.NewBookingDTO;
import com.yvolabs.airbnbclone.booking.domain.Booking;
import org.mapstruct.Mapper;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 18/09/2024
 */

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking newBookingToBooking(NewBookingDTO newBookingDTO);

    BookedDateDTO bookingToCheckAvailability(Booking booking);

}
