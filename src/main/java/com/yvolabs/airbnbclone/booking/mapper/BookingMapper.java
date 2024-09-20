package com.yvolabs.airbnbclone.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 18/09/2024
 */

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

}
