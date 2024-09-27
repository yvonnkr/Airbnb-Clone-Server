package com.yvolabs.airbnbclone.booking.application.dto;

import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.PriceVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 27/09/2024
 */

@Builder
public record BookedListingDTO(
        @Valid PictureDTO cover,
        @NotEmpty String location,
        @Valid BookedDateDTO dates,
        @Valid PriceVO totalPrice,
        @NotNull UUID bookingPublicId,
        @NotNull UUID listingPublicId
) {
}
